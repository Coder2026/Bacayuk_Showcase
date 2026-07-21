package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.Bank;

public record BankAccountResponse(
        String Id,
        String accountNumber,
        String accountName,
        BankResponse bank
) {
}
