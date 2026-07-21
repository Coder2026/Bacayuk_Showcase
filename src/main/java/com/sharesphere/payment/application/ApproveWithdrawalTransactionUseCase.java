package com.sharesphere.payment.application;


import com.sharesphere.payment.domain.ApproveWithdrawalRequest;
import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.domain.WithdrawalTransaction;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ApproveWithdrawalTransactionUseCase {

    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;


    @Transactional
    public void execute(String adminId, String withdrawalTransactionId, ApproveWithdrawalRequest request) {
        var transaction = walletTransactionRepository.findById(withdrawalTransactionId)
                .filter(t -> t instanceof WithdrawalTransaction)
                .map(t -> (WithdrawalTransaction) t)
                .orElseThrow(() -> new IllegalArgumentException("Withdrawal transaction not found"));

        User admin = userRepository.getReferenceById(adminId);

        if (transaction.getStatus() != WalletTransactionStatus.PENDING) {
            throw new IllegalStateException("Withdrawal transaction already processed");
        }

        BigDecimal walletBalance = transaction.getWallet().getBalance();
        if (walletBalance.compareTo(transaction.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        // Update status, admin, waktu, dan simpan proof key
        transaction.setStatus(WalletTransactionStatus.SUCCESS);
        transaction.setVerifiedAt(Instant.now());
        transaction.setVerifiedBy(admin);
        transaction.setTransferProofKey(request.proofKey());

        // Kurangi saldo user
        transaction.getWallet().setBalance(walletBalance.subtract(transaction.getAmount()));

        walletTransactionRepository.save(transaction);
    }
}
