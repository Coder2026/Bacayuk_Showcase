package com.sharesphere.post.dto;

import java.util.List;

public record CategoriesResponse(
        List<CategoryResponse> categories
) {}
