package com.sharesphere.share.service;


import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.share.dto.PresignedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Facade {

    private final S3Service s3Service;
    private final PostRepository postRepository;


    public PresignedResponse generateProfileUploadUrl(String userId) {
        String key = "photo/profile/" + userId + "/image.jpg";
        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }

    public PresignedResponse generateNewPostUploadUrl(String userId) {
        String key = "photo/post/" + userId + "/" + UUID.randomUUID() + "/image.jpg";
        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }

    public PresignedResponse generateTopUpProofUploadUrl(String userId, String transactionId) {
        String key = "top-up/" + userId + "/" + transactionId +"/image.jpg";
        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }

    public PresignedResponse generateUpdatePostUploadUrl(String postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        String key = post.getPhotoKey();
        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }

    public PresignedResponse generateTransactionProofUploadUrl(String userId, String transactionId) {
        String key = "transaction/" + userId + "/" + transactionId + UUID.randomUUID() + "/image.jpg";
        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }

    public PresignedResponse generateDisputeProofUploadUrl(String userId, String transactionId) {
        String key = "dispute/" + userId + "/" + transactionId + UUID.randomUUID()+ "/image.jpg";
        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }

    public PresignedResponse generateWithdrawalProofUploadUrl(String adminId, String withdrawalId) {
        String key = "withdrawal/proof/"
                + adminId + "/"
                + withdrawalId + "/"
                + UUID.randomUUID()
                + "/image.jpg";

        String url = s3Service.generatePresignedPutUrl(key);
        return new PresignedResponse(key, url);
    }
}
