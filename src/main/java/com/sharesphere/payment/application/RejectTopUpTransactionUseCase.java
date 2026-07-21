package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.TopUpTransaction;
import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RejectTopUpTransactionUseCase {

    private final WalletTransactionRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(String adminId, String walletTransactionId) {
        var transaction = repository.findById(walletTransactionId)
                .filter(t -> t instanceof TopUpTransaction)
                .map(t -> (TopUpTransaction) t)
                .orElseThrow(() -> new IllegalArgumentException("Top-up transaction not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        transaction.setStatus(WalletTransactionStatus.REJECTED);
        transaction.setVerifiedAt(Instant.now());
        transaction.setVerifiedBy(admin);
        repository.save(transaction);
    }
}
