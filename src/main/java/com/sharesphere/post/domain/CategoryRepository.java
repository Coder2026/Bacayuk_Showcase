package com.sharesphere.post.domain;
import java.util.List;

public interface CategoryRepository {
    Category getReferenceById(Long categoryId);
    List<Category> findAll();
}
