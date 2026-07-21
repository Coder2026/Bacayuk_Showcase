package com.sharesphere.payment.application;

import com.sharesphere.payment.adapter.WalletTransactionJpaRepository;
import com.sharesphere.payment.domain.TopUpTransaction;
import com.sharesphere.payment.domain.WithdrawalTransaction;
import com.sharesphere.payment.dto.TopUpTransactionDetail;
import com.sharesphere.payment.dto.WalletTransactionDetail;
import com.sharesphere.payment.dto.WithdrawalTransactionDetail;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GetWalletTransactionUseCase {

    private final WalletTransactionJpaRepository repository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public WalletTransactionDetail execute(String id) {


        var tx = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));

        return switch (tx) {
            case TopUpTransaction t -> new TopUpTransactionDetail(
                    t.getId(),
                    t.getType(),
                    t.getAmount(),
                    t.getStatus(),
                    t.getCreatedAt(),
                    t.getVerifiedBy() != null ? t.getVerifiedBy().getName() : null,
                    t.getPlatformBankAccount() != null
                            ? t.getPlatformBankAccount().getBank().getName() + " - " + t.getPlatformBankAccount().getAccountNumber()
                            : null,
                    s3Service.getPublicUrlFromKey(t.getProofKey()),
                    t.isConfirmed()
            );
            case WithdrawalTransaction w -> new WithdrawalTransactionDetail(
                    w.getId(),
                    w.getType(),
                    w.getAmount(),
                    w.getStatus(),
                    w.getCreatedAt(),
                    w.getVerifiedBy() != null ? w.getVerifiedBy().getName() : null,
                    w.getBank() != null ? w.getBank().getName() : null,
                    w.getAccountNumber(),
                    w.getAccountHolderName(),
                    s3Service.getPublicUrlFromKey(w.getTransferProofKey())
            );
            default -> throw new IllegalStateException("Unknown transaction type: " + tx.getClass());
        };
    }
}
