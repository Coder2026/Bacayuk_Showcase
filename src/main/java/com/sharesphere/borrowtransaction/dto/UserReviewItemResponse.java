package com.sharesphere.borrowtransaction.dto;

public record UserReviewItemResponse(
        UserProfileSummary reviewer,
        String comment,
        double rating
) {
}
