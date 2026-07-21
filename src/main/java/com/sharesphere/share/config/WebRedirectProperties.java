package com.sharesphere.share.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebRedirectProperties {

    @Value("${app.web.base-url}")
    private String baseUrl;

    public String buildUrl(String path, String message) {
        return baseUrl + "/" + path + "?message=" + message;
    }
}
