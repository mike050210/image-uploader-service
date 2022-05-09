package com.epam.aws.training.service.impl;

import com.epam.aws.training.controller.ImageController;
import com.epam.aws.training.entity.ImageEntity;
import com.epam.aws.training.model.Image;
import com.epam.aws.training.repository.ImageJpaRepository;
import com.epam.aws.training.service.ImageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Data
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageJpaRepository imageJpaRepository;
    private final S3Service s3Service;
    private final FileService fileService;
    private final SubscriptionServiceImpl subscriptionService;
    private final ModelMapper modelMapper;

    private static final String template = """
            A new image was uploaded to the repository.
            
            Image details:
               * Name: %s
               * Size: %d bytes
               * Extension: %s
               * Uploaded at: %s
            
            Open this this link to download the new image: %s
            """;

    @Override
    @Transactional
    public ResponseEntity<?> findAll() {
        List<ImageEntity> entities = imageJpaRepository.findAll();

        List<Image> clientModels = entities.stream()
                .map(entityModel -> modelMapper.map(entityModel, Image.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientModels, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> upload(MultipartFile multipartFile) {

        String filename = multipartFile.getOriginalFilename();

        // Verify existence
        imageJpaRepository.findByName(filename)
                .ifPresent(metadata -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is already present");
                });

        // Save to S3
        log.info("Uploading file {} to S3", filename);

        InputStream tempInput = fileService.getInputStream(multipartFile);
        s3Service.uploadObject(tempInput, filename, filename);

        // Store metadata to DB
        log.info("Storing metadata information for file {} to DB", filename);
        ImageEntity imageEntity;
        try {
            imageEntity = ImageEntity.builder()
                    .name(filename)
                    .fileExtension(fileService.getFileExtension(filename))
                    .size(multipartFile.getSize())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Error storing the metadata information for file " + filename);
        }
        log.info("Entity to save: " + imageEntity.toString());
        ImageEntity save = imageJpaRepository.save(imageEntity);

        log.info("Sending message to SQS queue for new file {}", filename);
        subscriptionService.sendMessageToQueue(createMessage(imageEntity));

        // Return response
        Image imageClientModel = modelMapper.map(save, Image.class);
        return new ResponseEntity<>(imageClientModel, HttpStatus.CREATED);
    }

    private String createMessage(ImageEntity imageEntity) {
        var downloadLink = linkTo(methodOn(ImageController.class).download(imageEntity.getName()));

        return String.format(template, imageEntity.getName(), imageEntity.getSize(), imageEntity.getFileExtension(), imageEntity.getUpdatedAt(), downloadLink);

        //return StringUtils.joinWith(":::", "Image was uploaded ", imageEntity.toString(), downloadLink);
    }

    @Override
    @Transactional
    public ResponseEntity<?> delete(String name) {
        Optional<ImageEntity> byName = imageJpaRepository.findByName(name);
        byName.ifPresent(imageEntity -> {
            s3Service.deleteObject(imageEntity.getName());
            imageJpaRepository.delete(imageEntity);
        });

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Transactional
    public ResponseEntity<?> getRandom() {
        Optional<ImageEntity> entityModel = imageJpaRepository.findRandomEntity();
        Image imageClientModel = modelMapper.map(entityModel.get(), Image.class);
        return new ResponseEntity<>(imageClientModel, HttpStatus.OK);
    }

    @Override
    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageEntity> imageEntity = imageJpaRepository.findByName(name);
        if (imageEntity.isEmpty() || imageEntity.get().getSize() == 0) {
            throw new RuntimeException("Object wasn't found or it's invalid");
        }

        try {
            return s3Service.downloadObject(imageEntity.get().getName());
        } catch (IOException e) {
            throw new RuntimeException("Error while getting object '" + name + "' from S3");
        }

    }
}
