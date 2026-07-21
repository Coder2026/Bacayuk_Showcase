package com.sharesphere.post.dto;


import com.sharesphere.post.domain.PostStatus;

import java.math.BigDecimal;
public record NearbyPostItem(
        String id,
        String title,
        String photoUrl,
        BigDecimal dailyRentFee,
        Double distanceKm,
        PostStatus status
) {
}
