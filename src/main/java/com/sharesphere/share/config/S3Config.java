package com.sharesphere.share.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class S3Config {
    private String bucket;
    private String region;
    private int expiry;
    private String publicUrlTemplate;
}
