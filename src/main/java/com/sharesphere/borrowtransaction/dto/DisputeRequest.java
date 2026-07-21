package com.sharesphere.borrowtransaction.dto;

import com.sharesphere.borrowtransaction.domain.DisputeType;

import java.util.List;

public record DisputeRequest(
        List<String> photoKeys,
        String disputeReason,
        DisputeType disputeType
) {
}
