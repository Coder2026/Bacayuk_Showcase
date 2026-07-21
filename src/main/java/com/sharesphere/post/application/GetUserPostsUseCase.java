package com.sharesphere.post.application;

import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.share.service.S3Service;
import com.sharesphere.post.dto.PostItem;
import com.sharesphere.post.dto.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserPostsUseCase {


    final private PostRepository postRepository;
    final private S3Service s3Service;
    public PostsResponse execute(String userId){

        List<Post> posts = postRepository.findByOwnerId(userId);

        List<PostItem> myPostResponse = posts.stream()
                .filter(post -> !post.isDeleted())
                .map(post -> new PostItem(post.getId(),
                        s3Service.getPublicUrlFromKey(post.getPhotoKey()),
                        post.getTitle(),
                        post.getDailyRentFee(),
                        post.getStatus()
                ))
                .toList();
        
        return new PostsResponse(myPostResponse);
    }
}
