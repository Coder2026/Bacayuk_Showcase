package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.WalletTransaction;

import java.util.List;

public record WalletTransactionsResponse(
        List<WalletTransactionSummary> transactions,
        int totalPages,
        long totalElements,
        int currentPage,
        int pageSize
) {
}
