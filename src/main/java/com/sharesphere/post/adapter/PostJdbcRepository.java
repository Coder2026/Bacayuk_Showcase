package com.sharesphere.post.adapter;

import com.sharesphere.post.dto.NearbyCursor;
import com.sharesphere.post.dto.NearbyPostItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostJdbcRepository {

    private final JdbcTemplate jdbcTemplate;


    private static final String NEARBY_QUERY_BASE = """
        WITH search_point AS (
            SELECT ST_SetSRID(ST_MakePoint(?, ?), 4326) as point
        ),
        nearby_candidates AS (
            SELECT\s
                p.id,
                p.title,
                p.photo_key,
                p.daily_rent_fee,
                p.status,
                u.name as owner_name,
                ST_Distance(u.location, sp.point) / 1000.0 as distance_km
            FROM posts p\s
            CROSS JOIN search_point sp
            INNER JOIN users u ON p.owner_id = u.id\s
            INNER JOIN categories c ON p.category_id = c.id
            WHERE p.is_deleted = false\s
              AND p.status = 'AVAILABLE'
              AND p.owner_id != ?
              AND u.location IS NOT NULL
              AND ST_DWithin(u.location, sp.point, ?)
        )
        SELECT * FROM nearby_candidates nc
        WHERE 1=1
       \s""";

    private static final String CURSOR_FILTER = """
        AND (nc.distance_km > ? OR (nc.distance_km = ? AND nc.id > ?))
        """;

    private static final String ORDER_LIMIT = """
        ORDER BY nc.distance_km, nc.id
        LIMIT ?
        """;

    public List<NearbyPostItem> findNearby(double latitude, double longitude,
                                           double radiusKm, NearbyCursor cursor, int limit, String currentUserId) {

        String sql = buildQuery(cursor != null);
        List<Object> params = buildQueryParams(longitude, latitude, currentUserId, radiusKm, cursor, limit);

        long startTime = System.currentTimeMillis();

        List<NearbyPostItem> results = jdbcTemplate.query(
            sql,
            NearbyPostRowMapper.INSTANCE,
                params.toArray()
        );

        logQueryPerformance(results.size(), radiusKm, startTime);

        return results;
    }

    // ✅ Simple & clear query building
    private String buildQuery(boolean hasCursor) {
        return NEARBY_QUERY_BASE +
               (hasCursor ? CURSOR_FILTER : "") +
               ORDER_LIMIT;
    }

    // ✅ Clear parameter building with documentation
    private List<Object> buildQueryParams(double longitude, double latitude, String currentUserId, double radiusKm,
                                         NearbyCursor cursor, int limit) {
        List<Object> params = new ArrayList<>();

        // Search point coordinates (for ST_MakePoint)
        params.add(longitude);
        params.add(latitude);


        // Filter out current user's posts
        params.add(currentUserId);

        // Spatial filter radius in meters (for ST_DWithin)
        params.add(radiusKm * 1000.0);
        
        // Cursor-based pagination parameters
        if (cursor != null) {
            params.add(cursor.distanceKm());    // distance comparison
            params.add(cursor.distanceKm());    // distance equality check  
            params.add(cursor.postId());        // ID comparison for ties
        }
        
        // Result limit
        params.add(limit);
        
        return params;
    }


    // ✅ Separated logging for better maintainability
    private void logQueryPerformance(int resultCount, double radiusKm, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        
        log.info("Spatial query: {} posts within {}km in {}ms", 
                resultCount, radiusKm, duration);
        
        // Performance monitoring
        if (duration > 100) {
            log.warn("Slow spatial query detected: {}ms execution time", duration);
        }

        if (resultCount == 0) {
            log.debug("No posts found within {}km radius", radiusKm);
        }
    }
}