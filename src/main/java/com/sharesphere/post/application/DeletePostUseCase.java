package com.sharesphere.post.application;

import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DeletePostUseCase {

    private  final PostRepository postRepository;
    public void execute(String userId, String postId){

        Post post = postRepository.getReferenceById(postId);

        if (post == null) {
            throw new IllegalArgumentException("Post tidak ditemukan");
        }

        if (post.isDeleted()) {
            throw new IllegalArgumentException("Post sudah dihapus");
        }


        if (!post.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Anda tidak memiliki izin untuk menghapus post ini");
        }

        post.setDeleted(true);
        postRepository.save(post);
    }
}
