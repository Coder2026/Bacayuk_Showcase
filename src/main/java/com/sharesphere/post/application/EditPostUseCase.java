package com.sharesphere.post.application;

import com.sharesphere.post.domain.Category;
import com.sharesphere.post.domain.CategoryRepository;
import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EditPostUseCase {


    final private PostRepository postRepository;
    final private CategoryRepository categoryRepository;

    public void execute(String userId, String postId,PostRequest postRequest){

        Category category = categoryRepository.getReferenceById(postRequest.categoryId());

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("Post tidak ditemukan"));

        if (post.isDeleted()){
            throw new IllegalArgumentException("Post sudah dihapus");
        }
        if (!post.getOwner().getId().equals(userId)){
            throw new IllegalArgumentException("Anda tidak memiliki izin untuk mengedit post ini");
        }
        post.update(postRequest,category);

        postRepository.save(post);
    }

}
