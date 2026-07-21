package com.sharesphere.post.dto;

/**
 * Compact parameter holder for nearby posts query to avoid long method signatures.
 */
public record NearbyPotsParams(
        double latitude,
        double longitude,
        double radiusKm,
        NearbyCursor cursor,
        int limit,
        String currentUserId
) {
}
