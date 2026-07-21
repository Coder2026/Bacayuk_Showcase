package com.sharesphere.payment.application;


import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.domain.WithdrawalTransaction;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RejectWithdrawalTransactionUseCase {


    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(String adminId, String withdrawalTransactionId) {

        var transaction = walletTransactionRepository.findById(withdrawalTransactionId)
                .filter(t -> t instanceof WithdrawalTransaction)
                .map(t -> (WithdrawalTransaction) t)
                .orElseThrow(() -> new IllegalArgumentException("Withdrawal transaction not found"));


        User admin = userRepository.getReferenceById(adminId);

        transaction.setStatus(WalletTransactionStatus.REJECTED);
        transaction.setVerifiedAt(Instant.now());
        transaction.setVerifiedBy(admin);


        walletTransactionRepository.save(transaction);
    }
}
