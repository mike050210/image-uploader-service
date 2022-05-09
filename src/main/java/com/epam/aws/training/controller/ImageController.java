package com.epam.aws.training.controller;

import com.epam.aws.training.service.impl.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {


    private final ImageServiceImpl imageService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return imageService.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        return imageService.upload(multipartFile);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> delete(@PathVariable String name) {
        return imageService.delete(name);
    }

    @GetMapping("/get-random")
    public ResponseEntity<?> getRandomImage() {
        return imageService.getRandom();
    }

    @GetMapping(value = "/download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> download(@PathVariable String name) {
        var data = imageService.getImage(name);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; fileName=\"" + name + "\"")
                .body(new ByteArrayResource(data));
    }
}
