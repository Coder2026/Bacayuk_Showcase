package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.TopUpTransaction;
import com.sharesphere.payment.domain.WalletTransaction;
import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.dto.ConfirmTopUpRequest;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmTopUpUseCase {

    private final WalletTransactionRepository walletTransactionRepository;

    public void execute(String userId,String walletTransactionId, ConfirmTopUpRequest request) {
        WalletTransaction transaction = walletTransactionRepository.findById(walletTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));


        if (!(transaction instanceof TopUpTransaction)) {
            throw new IllegalArgumentException("Transaksi bukan top-up");
        }

        // Ambil pemilik wallet
        String ownerId = transaction.getWallet().getOwner().getId();

        if (!ownerId.equals(userId)) {
            throw new IllegalStateException("User tidak berhak mengonfirmasi top-up ini");
        }

        ((TopUpTransaction) transaction).setConfirmed(true);
        ((TopUpTransaction) transaction).setProofKey(request.photoKey());

        walletTransactionRepository.save(transaction);
    }
}
