package com.sharesphere.usermanagement.domain;

import java.util.Map;

public interface EmailService {
    void sendVerificationEmail(String to, Map<String, String> data);
    void sendPasswordResetEmail(String to, Map<String, String> data);
}

