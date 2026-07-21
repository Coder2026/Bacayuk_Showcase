package com.sharesphere.borrowtransaction.dto;

import java.util.List;

public record BorrowListResponse(
        List<BorrowResponse> transactions
) {
}
