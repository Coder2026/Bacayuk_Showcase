package com.sharesphere.post.adapter;

import com.sharesphere.post.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category,Long> {
}
