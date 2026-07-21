package com.sharesphere.post.dto;

import java.util.List;

public record NearbyPostsResponse(
        List<NearbyPostItem> posts,
        PaginationInfo pagination
) {
    public record PaginationInfo(
            boolean hasNext,
            String nextCursor,
            int currentCount
    ) {}
}