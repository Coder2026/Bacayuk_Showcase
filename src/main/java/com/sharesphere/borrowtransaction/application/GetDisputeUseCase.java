package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.BorrowFinancialTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowFinancialTransactionRepository;
import com.sharesphere.borrowtransaction.domain.Dispute;
import com.sharesphere.borrowtransaction.domain.DisputeRepository;
import com.sharesphere.borrowtransaction.dto.DisputeResponse;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetDisputeUseCase {


    private final DisputeRepository disputeRepository;
    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final S3Service s3Service;

    public DisputeResponse execute(String disputeId){

        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new IllegalArgumentException("Dispute tidak ditemukan"));

        
        List<String> lenderEvidenceUrls = dispute.getLenderEvidenceKeys() == null ? null : dispute.getLenderEvidenceKeys().stream()
                .map(k -> k == null ? null : s3Service.generatePresignedGetUrl(k))
                .collect(Collectors.toList());

        List<String> borrowerEvidenceUrls = dispute.getBorrowerEvidenceKeys() == null ? null : dispute.getBorrowerEvidenceKeys().stream()
                .map(k -> k == null ? null : s3Service.generatePresignedGetUrl(k))
                .collect(Collectors.toList());


        BorrowFinancialTransaction financialTx =
                borrowFinancialTransactionRepository
                        .findByBorrowTransaction(dispute.getBorrowTransaction())
                        .orElseThrow(() ->
                                new IllegalStateException("Transaksi keuangan tidak ditemukan")
                        );

        return new DisputeResponse(
                dispute.getId(),
                dispute.getBorrowTransaction().getId(),
                dispute.getBorrowTransaction().getPost().getTitle(),
                dispute.getBorrowTransaction().getBorrower().getName(),
                dispute.getBorrowTransaction().getLender().getName(),
                dispute.getType(),
                dispute.getDisputeReason(),
                financialTx.getGuarantee(),
                lenderEvidenceUrls,
                borrowerEvidenceUrls,
                dispute.getResolutionNote(),
                dispute.getStatus(),
                dispute.getCreatedAt()
        );
    }
}
