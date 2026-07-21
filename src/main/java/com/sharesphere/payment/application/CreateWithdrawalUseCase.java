package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.*;
import com.sharesphere.payment.dto.CreateWithdrawalRequest;
import com.sharesphere.payment.dto.WithdrawalResponse;
import com.sharesphere.share.exception.BusinessException;
import com.sharesphere.share.exception.ConflictException;
import com.sharesphere.share.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class  CreateWithdrawalUseCase {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public WithdrawalResponse execute(String userId, CreateWithdrawalRequest request) {

        if (walletTransactionRepository.findActiveWithdrawal(userId, WalletTransactionStatus.PENDING).isPresent()) {
            throw new ConflictException("User already has a pending withdrawal");
        }

        Wallet wallet = walletRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));


        BankAccount bankAccount = bankAccountRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        if (wallet.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }


        if (request.amount().compareTo(BigDecimal.valueOf(10000)) < 0) {
            throw new BusinessException("Minimal penarikan adalah Rp10.000");
        }

        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setAmount(request.amount());
        transaction.setWallet(wallet);
        transaction.setCreatedAt(Instant.now());
        transaction.setBank(bankAccount.getBank());
        transaction.setAccountNumber(bankAccount.getAccountNumber());
        transaction.setAccountHolderName(bankAccount.getAccountName());
        transaction.setType(WalletTransactionType.WITHDRAWAL);
        transaction.setStatus(WalletTransactionStatus.PENDING);
        walletTransactionRepository.save(transaction);

        return new WithdrawalResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getStatus()
        );
    }
}