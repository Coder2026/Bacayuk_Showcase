package com.sharesphere.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

public record SearchPostsRequest(
        @Size(min = 1, max = 100, message = "Keyword harus antara 1-100 karakter")
        String keyword,
        Long categoryId,
        String cursor
) {


    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }
    
    public boolean hasCategory() {
        return categoryId != null;
    }
    
    public boolean hasCursor() {
        return cursor != null && !cursor.trim().isEmpty();
    }

    @JsonIgnore
    public String getProcessedKeyword() {
        return hasKeyword() ? "%" + keyword.trim().toLowerCase() + "%" : null;
    }
}
