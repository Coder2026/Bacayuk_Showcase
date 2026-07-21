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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetNearbyPostsUseCase {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NearbySearchConfig config;
    private final S3Service s3Service;
    @Transactional(readOnly = true)
    public NearbyPostsResponse execute(String userId, NearbyPostsRequest request) {

        long startTime = System.currentTimeMillis();

        Point userLocation = userRepository.findLocationByUserId(userId)
                .orElseThrow(() -> new UserLocationNotSetException("Lokasi pengguna tidak ditemukan. Silakan perbarui lokasi Anda di pengaturan profil."));

        double latitude = userLocation.getY();
        double longitude = userLocation.getX();
        NearbyCursor cursor = CursorUtils.decode(request.cursor());

        log.info("Nearby search for user: {} at lat:{}, lng:{}",
                userId, latitude, longitude);


        SearchResult result = cursor == null
                ? executeInitialSearch(userId,latitude, longitude)
                : executePaginationSearch(userId,latitude, longitude, cursor);

        return buildResponse(result, System.currentTimeMillis() - startTime);
    }

    private SearchResult executeInitialSearch(String userId, double latitude, double longitude) {

        double[] searchRadiuses = {
            config.getDefaultRadius(),       // 5km
            config.getSparseAreaRadius(),    // 25km
            config.getMaxRadius()            // 50km
        };


        List<NearbyPostItem> results = null;
        double usedRadius = searchRadiuses[0];


        for (double radius : searchRadiuses) {
            usedRadius = radius;

            NearbyPotsParams params = new NearbyPotsParams(
                    latitude,
                    longitude,
                    radius,
                    null,
                    config.getMaxResults(),
                    userId
            );

            results = postRepository.findNearby(params);

            log.info("Search with {}km radius: Found {} results", radius, results.size());


            if (results.size() >= config.getMinResultsThreshold() ||
                radius == config.getMaxRadius()) {
                break;
            }

            log.info("Results below threshold ({}), expanding search radius", config.getMinResultsThreshold());
        }

        return new SearchResult(results, usedRadius);
    }

    private SearchResult executePaginationSearch(String userId,double latitude, double longitude, NearbyCursor cursor) {

        double expandedRadius = cursor.lastUsedRadiusKm() * config.getPaginationMultiplier();
        expandedRadius = Math.min(expandedRadius, config.getMaxRadius());

        log.info("Pagination search: expanding from {}km to {}km", cursor.lastUsedRadiusKm(), expandedRadius);

        List<NearbyPostItem> results = postRepository.findNearby(
                new NearbyPotsParams(
                        latitude,
                        longitude,
                        expandedRadius,
                        cursor,
                        config.getMaxResults(),
                        userId
                )
        );

        return new SearchResult(results, expandedRadius);
    }

    private NearbyPostsResponse buildResponse(SearchResult result, long duration) {

        List<NearbyPostItem> allResults = result.posts();
        boolean hasNext = allResults.size() > config.getTargetResults();

        List<NearbyPostItem> posts = hasNext
                ? allResults.subList(0, config.getTargetResults())
                : allResults;


        List<NearbyPostItem> postsWithUrls = posts.stream()
                .map(post -> new NearbyPostItem(
                        post.id(),
                        post.title(),
                        s3Service.getPublicUrlFromKey(post.photoUrl()),
                        post.dailyRentFee(),
                        post.distanceKm(),
                        post.status()
                ))
                .toList();

        String nextCursor = hasNext && !posts.isEmpty()
                ? CursorUtils.fromLastPost(
                posts.get(posts.size() - 1),
                result.usedRadiusKm()
        )
                : null;

        log.info("Search completed in {}ms: {} items, radius: {}km",
                duration, postsWithUrls.size(), result.usedRadiusKm());

        return new NearbyPostsResponse(
                postsWithUrls,
                new NearbyPostsResponse.PaginationInfo(
                        hasNext,
                        nextCursor,
                        postsWithUrls.size()
                )
        );
    }

    private record SearchResult(
            List<NearbyPostItem> posts,
            Double usedRadiusKm
    ) {}
}