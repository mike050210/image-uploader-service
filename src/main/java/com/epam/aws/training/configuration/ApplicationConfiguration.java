package com.epam.aws.training.configuration;

import com.epam.aws.training.entity.ImageEntity;
import com.epam.aws.training.model.Image;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(ImageEntity.class, Image.class);
        return modelMapper;
    }
}
