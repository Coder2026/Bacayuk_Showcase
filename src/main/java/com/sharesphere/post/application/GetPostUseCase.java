package com.sharesphere.post.application;

import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.dto.CategoryResponse;
import com.sharesphere.post.dto.PostResponse;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPostUseCase {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    public PostResponse execute(String postId,String userId){

        Post post = postRepository.findById(postId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Gagal memuat informasi buku"));

        boolean isOwner = post.getOwner().getId().equals(userId);

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                post.getIsbn(),
                post.getPublishYear(),
                post.getPages(),
                post.getLanguage(),
                post.getDescription(),
                new CategoryResponse(post.getCategory().getId(), post.getCategory().getName()),
                post.getGuarantee(),
                post.getStatus(),
                post.getBookFormat(),
                post.getDailyRentFee(),
                s3Service.getPublicUrlFromKey(post.getPhotoKey()),
                isOwner,
                new PostResponse.OwnerInfo(
                        post.getOwner().getId(),
                        post.getOwner().getName(),
                        s3Service.getPublicUrlFromKey(post.getOwner().getPhotoKey())
                )
        );

    }
}
