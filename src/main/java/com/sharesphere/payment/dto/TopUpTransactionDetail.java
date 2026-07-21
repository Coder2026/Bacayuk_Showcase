package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.domain.WalletTransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TopUpTransactionDetail(
        String id,
        WalletTransactionType type,
        BigDecimal amount,
        WalletTransactionStatus status,
        Instant createdAt,
        String verifiedBy,
        String platformBankName,
        String transferProofUrl,
        boolean isConfirmed
) implements WalletTransactionDetail { }
