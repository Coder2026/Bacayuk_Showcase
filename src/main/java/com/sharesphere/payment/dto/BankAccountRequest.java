package com.sharesphere.payment.dto;

public record BankAccountRequest(
        Long bankId,
        String accountNumber,
        String accountName
) {
}
