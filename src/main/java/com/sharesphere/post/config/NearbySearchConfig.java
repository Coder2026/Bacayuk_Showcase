package com.sharesphere.post.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.nearby-search")
@Data
public class NearbySearchConfig {


    private double defaultRadius = 5.0;
    private double sparseAreaRadius = 25.0;
    private double maxRadius = 50.0;

    private int targetResults = 20;
    private int minResultsThreshold = 8;
    private int maxResults = 50;

    private double paginationMultiplier = 1.5;
}