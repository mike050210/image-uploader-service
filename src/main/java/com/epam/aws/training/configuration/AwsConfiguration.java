package com.epam.aws.training.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.epam.aws.training.configuration.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@Configuration
public class AwsConfiguration {

  private final AwsProperties awsProperties;

  @Bean
  public AmazonS3 getAmazonS3() {
    return AmazonS3ClientBuilder.standard()
        .withRegion(awsProperties.getRegion())
        .build();
  }

  @Bean
  public AmazonSNS snsClient(){
    return AmazonSNSClient
            .builder()
            .withRegion(awsProperties.getRegion())
            .build();
  }

  @Bean
  public AmazonSQS sqsClient(){
    return AmazonSQSClient
            .builder()
            .withRegion(awsProperties.getRegion())
            .build();
  }

  @Bean
  public AWSLambda awsLambda(){
    return AWSLambdaClientBuilder.defaultClient();
  }
}
