package com.sharesphere.post.application;

import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.dto.*;
import com.sharesphere.post.utils.CursorUtils;
import com.sharesphere.post.config.NearbySearchConfig;
import com.sharesphere.share.exception.UserLocationNotSetException;
import com.sharesphere.share.service.S3Service;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class SearchPostsUseCase {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final NearbySearchConfig config;


    @Transactional(readOnly = true)
    public SearchPostsResponse execute(SearchPostsRequest request, String currentUserId) {
        Point userLocation = userRepository.findLocationByUserId(currentUserId)
                .orElseThrow(() -> new UserLocationNotSetException("Lokasi pengguna tidak ditemukan. Silakan perbarui lokasi Anda di pengaturan profil."));

        double latitude = userLocation.getY();
        double longitude = userLocation.getX();

        SearchCursor cursor = request.hasCursor()
                ? decodeSearchCursor(request.cursor())
                : null;

        log.info("Post search for user: {} at lat:{}, lng:{} (keyword: '{}', category: '{}')",
                currentUserId, latitude, longitude,
                request.hasKeyword() ? request.keyword() : "none",
                request.hasCategory() ? request.categoryId() : "none");

        SearchPostsParams searchParams = new SearchPostsParams(
                request,
                cursor,
                currentUserId,
                latitude,
                longitude
        );

        List<SearchPostItem> allResults =
                postRepository.searchPosts(searchParams);

        boolean hasNext = allResults.size() > config.getTargetResults();
        List<SearchPostItem> posts = hasNext
                ? allResults.subList(0, config.getTargetResults())
                : allResults;

        List<SearchPostItem> postsWithUrls = posts.stream()
                .map(post -> new SearchPostItem(
                        post.id(),
                        post.title(),
                        s3Service.getPublicUrlFromKey(post.photoUrl()),
                        post.dailyRentFee(),
                        post.distanceKm(),
                        post.status()
                ))
                .toList();

        String nextCursor = (hasNext && !posts.isEmpty())
                ? CursorUtils.fromLastPost(posts.get(posts.size() - 1), config.getMaxRadius())
                : null;

        SearchPostsResponse.PaginationInfo pagination =
                new SearchPostsResponse.PaginationInfo(hasNext, nextCursor, postsWithUrls.size());

        log.info("Search completed: {} posts found", postsWithUrls.size());

        return new SearchPostsResponse(postsWithUrls, pagination);
    }

    private SearchCursor decodeSearchCursor(String cursor) {
        if (cursor == null || cursor.trim().isEmpty()) {
            return null;
        }

        try {
            String decoded = new String(java.util.Base64.getDecoder().decode(cursor), java.nio.charset.StandardCharsets.UTF_8);
            String[] parts = decoded.split(CursorUtils.DELIMITER);
            if (parts.length >= 2) {
                Double distanceKm = Double.parseDouble(parts[0]);
                String postId = parts[1];

                return new SearchCursor(distanceKm, postId);
            }
        } catch (Exception e) {
            log.warn("Invalid search cursor format: {}", cursor, e);
        }

        return null;
    }
}