package com.sharesphere.post.domain;

import com.sharesphere.post.dto.*;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(String postId);
    void save(Post post);
    Post getReferenceById(String postId);
    List<Post> findByOwnerId(String ownerId);
    List<NearbyPostItem> findNearby(NearbyPotsParams params);
    List<SearchPostItem> searchPosts(SearchPostsParams params);

}
