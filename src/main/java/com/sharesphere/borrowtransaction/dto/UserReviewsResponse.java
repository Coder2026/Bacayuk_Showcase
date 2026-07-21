package com.sharesphere.borrowtransaction.dto;

import java.util.List;

public record UserReviewsResponse(
        List<UserReviewItemResponse> reviews
) {
}
