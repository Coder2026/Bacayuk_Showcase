package com.sharesphere.borrowtransaction.dto;

public record BorrowStatusDetail(
        Long id,
        String name,
        String borrowerMessage,
        String lenderMessage
) {
}
