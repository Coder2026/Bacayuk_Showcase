package com.sharesphere.share.dto;

import io.swagger.v3.oas.annotations.media.Schema;



@Schema(description = "Generic API response wrapper")
public record Response <T>(
    String message,
    T data
){}
