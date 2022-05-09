package com.epam.aws.training.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.aws.training.configuration.properties.AwsProperties;
import com.epam.aws.training.service.impl.FileService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Data
@Service
public class S3Service {

    private final AmazonS3 s3;
    private final FileService fileService;
    private final AwsProperties awsProperties;

    public byte[] downloadObject(String objectName) throws IOException {
        checkBucketExists();
        checkObjectExits(objectName);

        S3Object o = s3.getObject(awsProperties.getS3BucketName(), objectName);
        return fileService.readBitmap(o);
    }

    public void uploadObject(InputStream file, String filename, String customName) {
        checkBucketExists();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("Name", filename);
        metadata.setContentType("image/jpg");
        PutObjectRequest request = new PutObjectRequest(awsProperties.getS3BucketName(), customName, file, metadata);
        request.setMetadata(metadata);
        s3.putObject(request);
    }

    public void deleteObject(String objectName) {
        checkBucketExists();
        checkObjectExits(objectName);
        s3.deleteObject(awsProperties.getS3BucketName(), objectName);
    }

    private void checkBucketExists() {
        if (!s3.doesBucketExistV2(awsProperties.getS3BucketName())) {
            s3.createBucket(awsProperties.getS3BucketName());
        }
    }

    private void checkObjectExits(String objectName) {
        if (!s3.doesObjectExist(awsProperties.getS3BucketName(), objectName)) {
            throw new RuntimeException("Error while checking if object exists");
        }
    }
}
