package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.Bank;
import com.sharesphere.payment.domain.WalletTransactionStatus;

import java.math.BigDecimal;

public record TopUpResponse(
        String paymentTransactionId,
        BigDecimal amount,
        PlatformBankAccountResponse bankAccount,
        WalletTransactionStatus status,
        boolean isConfirmed
) {
}
