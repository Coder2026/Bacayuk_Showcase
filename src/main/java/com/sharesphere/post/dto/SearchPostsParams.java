package com.sharesphere.post.dto;

/**
 * Compact parameter holder for full-text + nearby search to avoid long method signatures.
 */
public record SearchPostsParams(
        SearchPostsRequest request,
        SearchCursor cursor,
        String currentUserId,
        double latitude,
        double longitude
) {
}
