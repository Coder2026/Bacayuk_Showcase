package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;

import com.sharesphere.payment.dto.WalletBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWalletBalanceUseCase {
    private final WalletRepository walletRepository;

    public WalletBalance execute(String userId) {

        Wallet wallet = walletRepository.findByOwnerId(userId).orElseThrow(
                () -> new RuntimeException("Wallet not found")
        );

        return new WalletBalance(wallet.getBalance());
    }
}
