package com.sharesphere.post.utils;

import com.sharesphere.post.dto.NearbyCursor;
import com.sharesphere.post.dto.NearbyPostItem;
import com.sharesphere.post.dto.SearchPostItem;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class CursorUtils {

    public static final String DELIMITER = ":";

    public static String encode(Double distanceKm, String postId, Double usedRadiusKm) {
        if (distanceKm == null || postId == null) {
            return null;
        }

        String cursorData = String.join(DELIMITER,
                distanceKm.toString(),
                postId,
                usedRadiusKm != null ? usedRadiusKm.toString() : "null"
        );

        return Base64.getEncoder().encodeToString(cursorData.getBytes(StandardCharsets.UTF_8));
    }

    public static NearbyCursor decode(String cursor) {
        if (cursor == null || cursor.trim().isEmpty()) {
            return null;
        }

        try {
            String decoded = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
            String[] parts = decoded.split(DELIMITER);
            if (parts.length >= 2) {
                Double distanceKm = Double.parseDouble(parts[0]);
                String postId = parts[1];
                Double lastUsedRadius = parts.length > 2 && !parts[2].equals("null")
                        ? Double.parseDouble(parts[2])
                        : null;

                return new NearbyCursor(distanceKm, postId, lastUsedRadius);
            }
        } catch (Exception e) {
            log.warn("Invalid cursor format: {}", cursor, e);
        }

        return null;
    }

    // --- For Nearby ---
    public static String fromLastPost(NearbyPostItem lastPost, Double usedRadius) {
        if (lastPost == null) {
            return null;
        }
        return encode(lastPost.distanceKm(), lastPost.id(), usedRadius);
    }

    public static String fromLastPost(SearchPostItem lastPost, Double usedRadius) {
        if (lastPost == null) {
            return null;
        }
        return encode(lastPost.distanceKm(), lastPost.id(), usedRadius);
    }
}