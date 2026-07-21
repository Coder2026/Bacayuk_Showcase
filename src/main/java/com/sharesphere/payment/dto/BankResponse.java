package com.sharesphere.payment.dto;

public record BankResponse(
        Long id,
        String name,
        String code,
        String photoUrl
) {
}
