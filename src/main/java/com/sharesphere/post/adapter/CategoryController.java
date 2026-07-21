package com.sharesphere.post.adapter;

import com.sharesphere.post.application.CategoryService;
import com.sharesphere.post.dto.CategoriesResponse;
import com.sharesphere.share.dto.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private  final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Response<CategoriesResponse>> getAllCategories() {
        CategoriesResponse categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Kategori berhasil diambil", categories));
    }
}
