package com.sharesphere.borrowtransaction.dto;

import com.sharesphere.borrowtransaction.domain.DisputeStatus;
import com.sharesphere.borrowtransaction.domain.DisputeType;

import java.time.Instant;

public record DisputeSummary(
        String disputeId,
        String transactionId,
        String disputeItemName,
        String borrowerName,
        String lenderName,
        DisputeType disputeType,
        DisputeStatus status,
        Instant createdAt
) {

}
