package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.borrowtransaction.dto.DisputeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestDisputeUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final DisputeRepository disputeRepository;


    @Transactional
    public void execute(String borrowTransactionId, DisputeRequest disputeRequest){


        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        Long statusId = borrowTransaction.getStatus().getId();
        if (statusId != 5L && statusId != 6L) {
            throw new IllegalStateException("Transaksi tidak dapat diproses");
        }


        borrowTransaction.setStatus(borrowStatusRepository.getReferenceById(7L));
        borrowTransactionRepository.save(borrowTransaction);

        Dispute dispute = Dispute.builder()
                .id(UUID.randomUUID().toString())
                .borrowTransaction(borrowTransaction)
                .disputeReason(disputeRequest.disputeReason())
                .borrowerEvidenceKeys(borrowTransaction.getProofHandedOverKeys())
                .lenderEvidenceKeys(disputeRequest.photoKeys())
                .type(disputeRequest.disputeType())
                .status(DisputeStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        disputeRepository.save(dispute);
    }
}
