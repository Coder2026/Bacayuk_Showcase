package com.sharesphere.share.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record PresignedResponse (
    String key,
    String url
){}
