package com.sharesphere.usermanagement.dto;

public record PrivateUserResponse(
    String id,
    String username,
    String email,
    String photoUrl,
    Location location

) {
    public record Location(
        double longitude,
        double latitude
    ) {}
}