package com.sharesphere.post.adapter;

import com.sharesphere.post.application.*;
import com.sharesphere.post.dto.*;
import com.sharesphere.share.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final GetUserPostsUseCase getUserPostsUseCase;
    private final GetPostUseCase getPostUseCase;
    private final EditPostUseCase editPostUseCase;
    private final GetNearbyPostsUseCase getNearbyPostsUseCase;
    private final SearchPostsUseCase searchPostsUseCase;

    @PostMapping
    ResponseEntity<Response<String>> createPost(@RequestBody @Valid PostRequest postRequest, @AuthenticationPrincipal UserDetails currentUser) {
        createPostUseCase.execute(currentUser.getUsername(), postRequest);
        Response<String> response = new Response<>("Post berhasil dibuat", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<Response<PostsResponse>> getUserPosts(@PathVariable @NotBlank String userId){
        PostsResponse postsResponse = getUserPostsUseCase.execute(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Post Anda berhasil diambil", postsResponse));
    }


    @GetMapping("/{postId}")
    ResponseEntity<Response<PostResponse>> getPost(@PathVariable @NotBlank String postId,@AuthenticationPrincipal UserDetails currentUser){
        PostResponse postResponse = getPostUseCase.execute(postId,currentUser.getUsername());
        return  ResponseEntity.status(HttpStatus.OK).body(new Response<>("Post berhasil diambil",postResponse));
    }

    @PutMapping("/{postId}")
    ResponseEntity<Response<String>> editPost(@PathVariable @NotBlank String postId,@RequestBody @Valid PostRequest postRequest, @AuthenticationPrincipal UserDetails currentUser){
        editPostUseCase.execute(currentUser.getUsername(),postId, postRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(new Response<>("Post berhasil diedit",null));
    }
    @DeleteMapping("/{postId}")
    ResponseEntity<Response<String>> deletePost(@PathVariable @NotBlank String postId, @AuthenticationPrincipal UserDetails currentUser) {
        deletePostUseCase.execute(currentUser.getUsername(),postId);
        Response<String> response = new Response<>("Post berhasil dihapus",null);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/nearby")
    ResponseEntity<Response<NearbyPostsResponse>> getNearbyPosts(
            @ModelAttribute @Valid NearbyPostsRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {

        NearbyPostsResponse nearbyPostsResponse = getNearbyPostsUseCase.execute(currentUser.getUsername(), request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new Response<>("Berhasil mengambil item di sekitar Anda", nearbyPostsResponse)
        );
    }

    @GetMapping("/search")
    @Operation(
            summary = "Cari post dengan query parameters",
            description = "Alternative endpoint untuk pencarian menggunakan query parameters"
    )
    public ResponseEntity<Response<SearchPostsResponse>> searchPosts(
            @ModelAttribute SearchPostsRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {

        String currentUserId = currentUser.getUsername();
        SearchPostsResponse searchResult = searchPostsUseCase.execute(request, currentUserId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response<>("Pencarian berhasil", searchResult)
        );
    }
}
