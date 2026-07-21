package com.sharesphere.post.adapter;

import com.sharesphere.post.domain.PostStatus;
import com.sharesphere.post.dto.SearchCursor;
import com.sharesphere.post.dto.SearchPostItem;
import com.sharesphere.post.dto.SearchPostsRequest;
import com.sharesphere.post.config.NearbySearchConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchPostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NearbySearchConfig config;
    private static final int DEFAULT_LIMIT = 20;

    private static final String SEARCH_QUERY_BASE = """
        WITH search_point AS (
            SELECT ST_SetSRID(ST_MakePoint(?, ?), 4326) as point
        ),
        search_results AS (
            SELECT
                p.id,
                p.title,
                p.description,
                p.photo_key,
                p.daily_rent_fee,
                p.status,
                u.name as owner_name,
                c.id AS category_id,
                ST_Distance(u.location, sp.point) / 1000.0 as distance_km
            FROM posts p
            CROSS JOIN search_point sp
            INNER JOIN users u ON p.owner_id = u.id
            INNER JOIN categories c ON p.category_id = c.id
            WHERE p.is_deleted = false
              AND p.owner_id != ?
              AND p.status = 'AVAILABLE'
              AND u.location IS NOT NULL
              AND ST_DWithin(u.location, sp.point, ?)
        )
        SELECT * FROM search_results sr
        WHERE 1=1
        """;

    private static final String CURSOR_FILTER = """
    AND (sr.distance_km > ? OR (sr.distance_km = ? AND sr.id > ?))
    """;

    private static final String ORDER_LIMIT = """
        ORDER BY sr.distance_km ASC, sr.id ASC
        LIMIT ?
        """;


    public List<SearchPostItem> searchPosts(SearchPostsRequest request, SearchCursor cursor,
                                            String currentUserId, double latitude, double longitude) {

        String sql = buildSearchQuery(request, cursor != null);
        List<Object> params = buildSearchParams(request, cursor, currentUserId, longitude, latitude);

        long startTime = System.currentTimeMillis();

        List<SearchPostItem> results = jdbcTemplate.query(
                sql,
                SearchPostRowMapper.INSTANCE,
                params.toArray()
        );

        logSearchPerformance(results.size(), request, startTime);

        return results;
    }


    private String buildSearchQuery(SearchPostsRequest request, boolean hasCursor) {
        StringBuilder query = new StringBuilder(SEARCH_QUERY_BASE);

        if (request.hasKeyword()) {
            query.append(" AND (LOWER(sr.title) LIKE ? OR LOWER(sr.description) LIKE ?) ");
        }

        if (request.hasCategory()) {
            query.append(" AND sr.category_id = ? ");
        }

        if (hasCursor) {
            query.append(CURSOR_FILTER);
        }

        query.append(ORDER_LIMIT);

        return query.toString();
    }

    private List<Object> buildSearchParams(SearchPostsRequest request, SearchCursor cursor,
                                           String currentUserId, double longitude, double latitude) {
        List<Object> params = new ArrayList<>();

        // Search point coordinates
        params.add(longitude);
        params.add(latitude);

        // Filter current user
        params.add(currentUserId);

        // Radius (menggunakan config.getMaxRadius() dari backend)
        params.add(config.getMaxRadius() * 1000.0);

        // Keyword filter
        if (request.hasKeyword()) {
            String processedKeyword = request.getProcessedKeyword();
            params.add(processedKeyword);
            params.add(processedKeyword);
        }


        // Category filter
        if (request.hasCategory()) {
            params.add(request.categoryId());
        }

        // Cursor
        if (cursor != null) {
            params.add(cursor.distanceKm());
            params.add(cursor.distanceKm());
            params.add(cursor.postId());
        }
        // Limit
        params.add(DEFAULT_LIMIT + 1);
        return params;
    }

    private void logSearchPerformance(int resultCount, SearchPostsRequest request, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        log.info("Search query: {} results in {}ms (radius: {}km, keyword: '{}', category: '{}')",
                resultCount, duration, config.getMaxRadius(),
                request.hasKeyword() ? request.keyword() : "none",
                request.hasCategory() ? request.categoryId() : "none");

        if (duration > 150) {
            log.warn("Slow search query: {}ms", duration);
        }
    }

    public enum SearchPostRowMapper implements RowMapper<SearchPostItem> {
        INSTANCE;

        @Override
        public SearchPostItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SearchPostItem(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("photo_key"),
                    rs.getBigDecimal("daily_rent_fee"),
                    rs.getDouble("distance_km"),
                    Enum.valueOf(PostStatus.class, rs.getString("status"))
            );
        }
    }
}