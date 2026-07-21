package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.dto.BankResponse;
import com.sharesphere.payment.dto.PlatformBankAccountResponse;
import com.sharesphere.payment.dto.TopUpResponse;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetActiveTopUpUseCase {
    private  final WalletTransactionRepository walletTransactionRepository;
    private  final S3Service s3Service;
    public TopUpResponse execute(String userId){
        return walletTransactionRepository.findActiveTopUp(userId, WalletTransactionStatus.PENDING)
                .map(tx -> new TopUpResponse(
                        tx.getId(),
                        tx.getAmount(),
                        new PlatformBankAccountResponse(
                                tx.getPlatformBankAccount().getAccountNumber(),
                                tx.getPlatformBankAccount().getAccountName(),
                                new BankResponse(
                                        tx.getPlatformBankAccount().getBank().getId(),
                                        tx.getPlatformBankAccount().getBank().getName(),
                                        tx.getPlatformBankAccount().getBank().getCode(),
                                        s3Service.getPublicUrlFromKey(
                                                tx.getPlatformBankAccount().getBank().getPhotoKey()
                                        )
                                )
                        ),
                        tx.getStatus(),
                        tx.isConfirmed()
                ))
                .orElse(null);
    }
}
