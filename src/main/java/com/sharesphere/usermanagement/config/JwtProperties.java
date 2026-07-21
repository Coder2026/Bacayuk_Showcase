package com.sharesphere.usermanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("jwt")
@Component
public class JwtProperties {
    private String secret;
    private Exp email = new Exp();
    private Exp access = new Exp();
    private Exp refresh = new Exp();
    private Exp onetime = new Exp();
    private Exp passwordReset = new Exp();

    @Data
    public static class Exp {
        private long expMinutes;
        private long expHours;
        private long expDays;
    }
}
