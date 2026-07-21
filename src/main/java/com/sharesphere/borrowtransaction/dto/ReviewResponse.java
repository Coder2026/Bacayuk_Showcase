package com.sharesphere.borrowtransaction.dto;

public record ReviewResponse(
        String reviewId,
        String comment,
        double rating
) {}
