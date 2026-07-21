package com.sharesphere.borrowtransaction.dto;

import com.sharesphere.borrowtransaction.domain.TransactionRole;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record BorrowDetailResponse(
        String borrowTransactionId,
        Instant startDate,
        Instant endDate,
        Instant returnedDate,
        UserSummary borrower,
        UserSummary lender,
        PostSummary post,
        List<String> proofHandedOverKeys,
        List<String> proofReturnedKeys,
        String handoverNotes,
        String returnNotes,
        Double meetingPointLatitude,
        Double meetingPointLongitude,
        BorrowStatusDetail status,
        TransactionRole role,
        BigDecimal dailyRent,
        BigDecimal guarantee,
        BigDecimal totalAmount,
        String reviewId
) {
}
