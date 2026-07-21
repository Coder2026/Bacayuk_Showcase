package com.sharesphere.borrowtransaction.dto;

import java.time.Instant;

public record BorrowRequest(
        String postId,
        double latitude,
        double longitude,
        Instant startDate,
        Instant endDate
) {
}
