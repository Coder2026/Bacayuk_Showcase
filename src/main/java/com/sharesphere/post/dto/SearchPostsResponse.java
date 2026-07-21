package com.sharesphere.post.dto;

import java.util.List;

public record SearchPostsResponse(
        List<SearchPostItem> posts,
        SearchPostsResponse.PaginationInfo pagination
) {
    public record PaginationInfo(
            boolean hasNext,
            String nextCursor,
            int currentCount
    ) {}
}
