package com.sharesphere.post.dto;

public record NearbyCursor(
        Double distanceKm,
        String postId,
        Double lastUsedRadiusKm
) {

    public NearbyCursor(Double distanceKm, String postId) {
        this(distanceKm, postId, null);
    }
}