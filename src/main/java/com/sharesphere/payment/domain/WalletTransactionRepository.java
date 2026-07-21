package com.sharesphere.payment.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletTransactionRepository {
    WalletTransaction save(WalletTransaction walletTransaction);
    Optional<WalletTransaction> findById(String id);
    boolean existsByAmountAndStatus(BigDecimal amount, WalletTransactionStatus status);
    Optional<TopUpTransaction> findActiveTopUp(String userId, WalletTransactionStatus status);
    Optional<WithdrawalTransaction> findActiveWithdrawal(String userId, WalletTransactionStatus status);
    Page<WalletTransaction> findByFilters(
           WalletTransactionStatus status,
           WalletTransactionType type,
           String keyword,
           Pageable pageable
    );
}
