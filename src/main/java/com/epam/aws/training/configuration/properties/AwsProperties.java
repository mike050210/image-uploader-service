package com.epam.aws.training.configuration.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "aws")
@Component
public class AwsProperties {
    private String region;

    private String s3BucketName;
    private String sqsQueueUrl;
    private String snsTopicArn;
    private String lambdaFunctionArn;
}
