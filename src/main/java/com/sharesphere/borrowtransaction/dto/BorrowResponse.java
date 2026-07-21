package com.sharesphere.borrowtransaction.dto;

import java.time.Instant;

public record BorrowResponse(
        String borrowTransactionId,
        Instant startDate,
        Instant endDate,
        UserSummary borrower,
        UserSummary lender,
        PostSummary post,
        BorrowStatusSummary status
) {
}
