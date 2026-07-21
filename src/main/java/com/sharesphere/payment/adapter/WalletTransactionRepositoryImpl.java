package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletTransactionRepositoryImpl implements WalletTransactionRepository {

    private final WalletTransactionJpaRepository walletTransactionJpaRepository;
    @Override
    public WalletTransaction save(WalletTransaction walletTransaction) {
        return walletTransactionJpaRepository.save(walletTransaction);
    }

    @Override
    public Optional<WalletTransaction> findById(String id) {
        return walletTransactionJpaRepository.findById(id);
    }

    @Override
    public boolean existsByAmountAndStatus(BigDecimal amount, WalletTransactionStatus status) {
        return walletTransactionJpaRepository.existsTopUpByAmountAndStatus(amount, status);
    }

    @Override
    public Optional<TopUpTransaction> findActiveTopUp(String userId, WalletTransactionStatus status) {
        return walletTransactionJpaRepository.findActiveTopUp(userId, status);
    }

    @Override
    public Optional<WithdrawalTransaction> findActiveWithdrawal(String userId, WalletTransactionStatus status) {
        return walletTransactionJpaRepository.findActiveWithdrawal(userId, status);
    }

    @Override
    public Page<WalletTransaction> findByFilters(WalletTransactionStatus status, WalletTransactionType type, String keyword, Pageable pageable) {
        return walletTransactionJpaRepository.findByFilters(status, type, keyword, pageable);
    }


}


