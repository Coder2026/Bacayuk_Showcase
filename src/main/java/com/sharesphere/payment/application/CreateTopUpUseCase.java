package com.sharesphere.payment.application;

import com.sharesphere.payment.adapter.WalletJpaRepository;
import com.sharesphere.payment.domain.*;
import com.sharesphere.payment.dto.BankResponse;
import com.sharesphere.payment.dto.CreateTopUpRequest;
import com.sharesphere.payment.dto.PlatformBankAccountResponse;
import com.sharesphere.payment.dto.TopUpResponse;
import com.sharesphere.share.exception.BusinessException;
import com.sharesphere.share.exception.ConflictException;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class CreateTopUpUseCase {
    private final WalletJpaRepository walletJpaRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final PlatformBankAccountRepository platformBankAccountRepository;
    private final S3Service s3Service;

    public TopUpResponse execute(String userId, CreateTopUpRequest request) {



        if (walletTransactionRepository.findActiveTopUp(userId, WalletTransactionStatus.PENDING).isPresent()) {
            throw new ConflictException("User already has a pending top-up");
        }

        Wallet wallet = walletJpaRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (request.amount().compareTo(BigDecimal.valueOf(10000)) < 0) {
            throw new BusinessException("Minimal penarikan adalah Rp10.000");
        }

        BigDecimal finalAmount = generateUniqueFinalAmount(request.amount());

        PlatformBankAccount platformBankAccount = platformBankAccountRepository.findByBankId(request.bankId());
        TopUpTransaction topUpTransaction = new TopUpTransaction();
        topUpTransaction.setId(UUID.randomUUID().toString());
        topUpTransaction.setWallet(wallet);
        topUpTransaction.setAmount(finalAmount.setScale(0, RoundingMode.DOWN));
        topUpTransaction.setCreatedAt(Instant.now());
        topUpTransaction.setPlatformBankAccount(platformBankAccount);
        topUpTransaction.setType(WalletTransactionType.TOP_UP);
        topUpTransaction.setConfirmed(false);
        topUpTransaction.setStatus(WalletTransactionStatus.PENDING);

        TopUpTransaction savedTransaction = (TopUpTransaction) walletTransactionRepository.save(topUpTransaction);

        return new TopUpResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                new PlatformBankAccountResponse(
                        platformBankAccount.getAccountNumber(),
                        platformBankAccount.getAccountName(),
                        new BankResponse(
                                platformBankAccount.getBank().getId(),
                                platformBankAccount.getBank().getName(),
                                platformBankAccount.getBank().getCode(),
                                s3Service.getPublicUrlFromKey(platformBankAccount.getBank().getPhotoKey())
                        )
                ),
                savedTransaction.getStatus(),
                savedTransaction.isConfirmed()
        );
    }

    private BigDecimal generateUniqueFinalAmount(BigDecimal baseAmount) {
        int retries = 0;
        while (retries < 10) {
            int unique = ThreadLocalRandom.current().nextInt(100, 500);
            BigDecimal finalAmount = baseAmount.add(BigDecimal.valueOf(unique));

            boolean exists = walletTransactionRepository
                    .existsByAmountAndStatus(finalAmount, WalletTransactionStatus.PENDING);

            if (!exists) {
                return finalAmount;
            }
            retries++;
        }
        throw new BusinessException("Failed to generate unique finalAmount");
    }

}
