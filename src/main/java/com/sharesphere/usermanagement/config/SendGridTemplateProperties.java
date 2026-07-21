package com.sharesphere.usermanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("sendgrid.template")
@Component
public class SendGridTemplateProperties {
    private String verification;
    private String resetPassword;
}
