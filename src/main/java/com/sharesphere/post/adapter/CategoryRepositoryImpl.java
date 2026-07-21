package com.sharesphere.post.adapter;

import com.sharesphere.post.domain.Category;
import com.sharesphere.post.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private  final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category getReferenceById(Long categoryId) {
        return categoryJpaRepository.getReferenceById(categoryId);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }
}
