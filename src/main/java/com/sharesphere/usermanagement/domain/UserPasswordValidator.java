package com.sharesphere.usermanagement.domain;

import org.springframework.stereotype.Component;

@Component
public class UserPasswordValidator {
    public void validate(String password){


        if (password.length() < 8) {
            throw new IllegalArgumentException("Password harus minimal 8 karakter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password harus mengandung huruf kecil");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password harus mengandung huruf besar");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password harus mengandung angka");
        }

    }}
