package com.sharesphere.borrowtransaction.dto;

import java.util.List;

public record DisputesResponse(
        List<DisputeSummary> disputes,
        int currentPage,
        int totalPages,
        long totalDisputes,
        int pages) {
}
