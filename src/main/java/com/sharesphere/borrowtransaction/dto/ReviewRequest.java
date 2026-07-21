package com.sharesphere.borrowtransaction.dto;

public record ReviewRequest(
        String comment,
        int rating
) {
}
