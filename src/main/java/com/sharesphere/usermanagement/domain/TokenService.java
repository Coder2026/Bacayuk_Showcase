package com.sharesphere.usermanagement.domain;


import com.sharesphere.usermanagement.dto.TokenPair;
import io.jsonwebtoken.Claims;

public interface TokenService {
    String generateEmailVerification(String userId);
    String generateOneTimeTokenLogin(String userId);
    String generatePasswordResetToken(String userId);
    String generateAccessToken(String userId);
    TokenPair createRefreshToken(User user);
    long getTtlAccessToken();
    Claims validateToken(String token,TokenType tokenType);
}
