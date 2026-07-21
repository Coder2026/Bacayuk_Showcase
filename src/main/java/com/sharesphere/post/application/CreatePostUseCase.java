package com.sharesphere.post.application;

import com.sharesphere.post.domain.Category;
import com.sharesphere.post.domain.CategoryRepository;
import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.dto.PostRequest;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class CreatePostUseCase {

    CategoryRepository categoryRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    public void execute(String userId, PostRequest postRequest){
        Category category = categoryRepository.getReferenceById(postRequest.categoryId());
        User user = userRepository.getReferenceById(userId);
        Post post = Post.builder()
                .id(UUID.randomUUID().toString())
                .title(postRequest.title())
                .author(postRequest.author())
                .isbn(postRequest.isbn())
                .bookFormat(postRequest.bookFormat())
                .pages(postRequest.pages())
                .publishYear(postRequest.publishYear())
                .dailyRentFee(postRequest.dailyRentFee())
                .language(postRequest.language())
                .category(category)
                .description(postRequest.description())
                .owner(user)
                .guarantee(postRequest.guarantee())
                .photoKey(postRequest.photoKey())
                .isDeleted(false)
                .build();
        postRepository.save(post);
    }

}
