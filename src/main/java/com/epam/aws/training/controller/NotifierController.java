package com.epam.aws.training.controller;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.epam.aws.training.configuration.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifier")
public class NotifierController {

    private final AWSLambda awsLambda;
    private final AwsProperties properties;

    @PutMapping
    public ResponseEntity<Object> executeLambdaFunction() {
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(properties.getLambdaFunctionArn())
                .withPayload("{\"detail-type\": \"Web Application\"}");
        awsLambda.invoke(invokeRequest);

        return ResponseEntity.ok().build();
    }

}
