package com.epam.aws.training.service;

import com.epam.aws.training.model.AwsInfoResponse;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;

@Service
public class AwsInfoService {

    public AwsInfoResponse getAwsInfo() {
        String region = EC2MetadataUtils.getEC2InstanceRegion();
        String az = EC2MetadataUtils.getAvailabilityZone();
        return AwsInfoResponse.builder().region(region).availabilityZone(az).build();
    }
}
