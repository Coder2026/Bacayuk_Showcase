package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.WalletTransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record WalletTransactionSummary(
        String id,
        String type,
        String userName,
        BigDecimal amount,
        WalletTransactionStatus status,
        Instant createdAt
) {
}
