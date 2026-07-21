package com.sharesphere.borrowtransaction.dto;

import com.sharesphere.borrowtransaction.domain.DisputeStatus;

public record GetDisputesRequest(
        DisputeStatus status,
        String keyword,
        int page,
        int size
) {}
