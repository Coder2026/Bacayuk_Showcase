package com.sharesphere.share.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LinkBuilder {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.web.base-url}")
    private  String webUrl;

    // Keep legacy name used by tests
    @Value("${app.front-url-path.magic-login}")
    private String loginCallbackPath;

    @Value("${app.front-url-path.reset-password}")
    private String resetPasswordPath;

    public String verificationLink(String token) {
        return UriComponentsBuilder.fromUri(URI.create(baseUrl))
                .path("/auth/verify")
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    // New method expected by tests
    public String buildAutoLoginUrl(String token) {
        return UriComponentsBuilder.fromUriString(webUrl)
                .path("/auth/")
                .path(loginCallbackPath)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    // Keep existing API for other usages
    public String buildMagicLoginUrl(String token) {
        return UriComponentsBuilder.fromUriString(webUrl)
                .path("/auth/")
                .path(loginCallbackPath)
                .queryParam("token", token)
                .build()
                .toUriString();
    }



    public String passwordResetLink(String token) {
        return UriComponentsBuilder.fromUriString(webUrl)
                .path("/auth/")
                .path(resetPasswordPath)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
