package com.sharesphere.usermanagement.dto;

public record LoginResponse(
        TokenResponse token,
        PrivateUserResponse user
) {
}
