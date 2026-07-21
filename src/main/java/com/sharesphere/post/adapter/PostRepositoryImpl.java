package com.sharesphere.post.adapter;

import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;
    private  final PostJdbcRepository postJdbcRepository;
    private final SearchPostRepository searchPostRepository;


    @Override
    public Optional<Post> findById(String postId) {
        return postJpaRepository.findById(postId);

    }


    @Override
    public void save(Post post) {
        postJpaRepository.save(post);
    }

    @Override
    public Post getReferenceById(String postId) {
        return postJpaRepository.getReferenceById(postId);
    }

    @Override
    public List<Post> findByOwnerId(String ownerId) {
        return postJpaRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<NearbyPostItem> findNearby(NearbyPotsParams params) {
        return postJdbcRepository.findNearby(
                params.latitude(),
                params.longitude(),
                params.radiusKm(),
                params.cursor(),
                params.limit(),
                params.currentUserId()
        );
    }

    @Override
    public List<SearchPostItem> searchPosts(SearchPostsParams params) {
        return searchPostRepository.searchPosts(
                params.request(),
                params.cursor(),
                params.currentUserId(),
                params.latitude(),
                params.longitude()
        );
    }
}
