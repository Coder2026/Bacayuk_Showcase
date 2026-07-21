package com.sharesphere.post.dto;

import com.sharesphere.post.domain.BookFormat;
import com.sharesphere.post.domain.PostStatus;

import java.math.BigDecimal;

public record PostResponse(
        String id,
        String title,
        String author,
        String isbn,
        Integer publishYear,
        Integer pages,
        String language,
        String description,
        CategoryResponse category,
        BigDecimal guarantee,
        PostStatus status,
        BookFormat bookFormat,
        BigDecimal dailyRentFee,
        String photoUrl,
        boolean isOwner,
        OwnerInfo owner
) {
    public record OwnerInfo(
            String id,
            String username,
            String photoUrl
    ) {}
}