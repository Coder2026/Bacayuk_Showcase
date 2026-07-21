package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.Bank;

public record PlatformBankAccountResponse(
        String accountNumber,
        String accountName,
        BankResponse bank
) {
}