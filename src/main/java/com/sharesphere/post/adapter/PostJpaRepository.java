package com.sharesphere.post.adapter;


import com.sharesphere.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post,String> {
    List<Post> findByOwnerId(String userId);
}
