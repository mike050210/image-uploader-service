package com.epam.aws.training.controller;

import com.epam.aws.training.model.AwsInfoResponse;
import com.epam.aws.training.service.impl.AwsInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/info")
public class InfoController {
    private final AwsInfoService awsInfoService;

    @GetMapping("/aws-info")
    AwsInfoResponse getAwsInfo() {
        return awsInfoService.getAwsInfo();
    }
}
