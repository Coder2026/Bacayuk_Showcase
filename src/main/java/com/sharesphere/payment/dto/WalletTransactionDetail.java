package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.domain.WalletTransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public sealed interface WalletTransactionDetail
        permits TopUpTransactionDetail, WithdrawalTransactionDetail {


    String id();
    WalletTransactionType type();
    BigDecimal amount();
    WalletTransactionStatus status();
    Instant createdAt();
    String verifiedBy();
}
