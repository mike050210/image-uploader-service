package com.epam.aws.training.controller;

import com.epam.aws.training.model.AwsInfoResponse;
import com.epam.aws.training.service.AwsInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aws-info")
public class AwsTrainingController {

    private final AwsInfoService awsInfoService;

    public AwsTrainingController(AwsInfoService awsInfoService) {
        this.awsInfoService = awsInfoService;
    }

    @GetMapping
    AwsInfoResponse getAwsInfo() {
        return awsInfoService.getAwsInfo();
    }
}
