package com.sharesphere.usermanagement.dto;


public record PublicUserResponse(
    String id,
    String username,
    String photoUrl
) { }