package com.sharesphere.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token tidak boleh kosong")
        String refreshToken
){}
