package com.sharesphere.post.application;

import com.sharesphere.post.domain.CategoryRepository;
import com.sharesphere.post.dto.CategoryResponse;
import com.sharesphere.post.dto.CategoriesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoriesResponse getAllCategories() {
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
        return new CategoriesResponse(categories);
    }
}