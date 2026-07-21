package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.WalletTransactionStatus;

import java.math.BigDecimal;

public record WithdrawalResponse(
        String paymentTransactionId,
        BigDecimal amount,
        WalletTransactionStatus status
) {
}
