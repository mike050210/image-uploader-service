package com.epam.aws.training.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ResponseEntity<?> findAll();

    ResponseEntity<?> upload(MultipartFile multipartFile);

    ResponseEntity<?> delete(String name);

    byte[] getImage(String name);

    ResponseEntity<?> getRandom();
}
