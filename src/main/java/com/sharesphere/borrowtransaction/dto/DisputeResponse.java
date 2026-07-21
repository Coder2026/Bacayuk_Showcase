package com.sharesphere.borrowtransaction.dto;

import com.sharesphere.borrowtransaction.domain.DisputeStatus;
import com.sharesphere.borrowtransaction.domain.DisputeType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record DisputeResponse(
        String disputeId,
        String transactionId,
        String disputeItemName,
        String borrowerName,
        String lenderName,
        DisputeType disputeType,
        String disputeReason,
        BigDecimal guarantee,
        List<String> lenderEvidenceUrls,
        List<String> borrowerEvidenceUrls,
        String adminComments,
        DisputeStatus status,
        Instant createdAt
) {
}
