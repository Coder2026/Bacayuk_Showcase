package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.dto.WithdrawalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetActiveWithdrawalUseCase {

    private final WalletTransactionRepository walletTransactionRepository;

    public WithdrawalResponse execute(String userId) {
        return walletTransactionRepository
                .findActiveWithdrawal(userId, WalletTransactionStatus.PENDING)
                .map(tx -> new WithdrawalResponse(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getStatus()
                ))
                .orElse(null);
    }
}