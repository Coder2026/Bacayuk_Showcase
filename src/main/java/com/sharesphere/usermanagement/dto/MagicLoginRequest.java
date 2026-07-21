package com.sharesphere.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;


public record MagicLoginRequest(
    @NotBlank
    String oneTimeToken
){}
