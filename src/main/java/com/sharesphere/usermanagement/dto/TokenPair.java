package com.sharesphere.usermanagement.dto;

import com.sharesphere.usermanagement.domain.Token;

public record TokenPair(
        Token refreshToken,
        String RawToken
) {
}
