package com.epam.aws.training.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AwsInfoResponse {
    private String region;
    private String availabilityZone;
}
