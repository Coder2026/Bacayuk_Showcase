package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.TopUpTransaction;
import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelTopUpUseCase {
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    public void execute(String userId, String transactionId) {
        TopUpTransaction tx = walletTransactionRepository.findById(transactionId)
                .filter(t -> t instanceof TopUpTransaction)
                .map(t -> (TopUpTransaction) t)
                .orElseThrow(() -> new IllegalArgumentException("Top-up transaction not found"));

        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("User tidak ditemukan"));
        if (!tx.getWallet().getOwner().getId().equals(user.getId())) {
            throw new IllegalStateException("User not authorized to cancel this transaction");
        }

        if (tx.getStatus() != WalletTransactionStatus.PENDING) {
            throw new IllegalStateException("Only pending transactions can be canceled");
        }

        tx.setStatus(WalletTransactionStatus.CANCELLED);

        walletTransactionRepository.save(tx);
    }
}
