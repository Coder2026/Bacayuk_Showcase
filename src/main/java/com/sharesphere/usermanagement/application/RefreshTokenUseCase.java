package com.sharesphere.usermanagement.application;

import com.sharesphere.share.dto.Response;
import com.sharesphere.usermanagement.domain.TokenType;
import com.sharesphere.usermanagement.domain.*;
import com.sharesphere.usermanagement.dto.RefreshTokenRequest;
import com.sharesphere.usermanagement.dto.TokenPair;
import com.sharesphere.usermanagement.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenUseCase {
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;



    public TokenResponse execute(RefreshTokenRequest refreshTokenRequest) {

        Claims claims = tokenService.validateToken(refreshTokenRequest.refreshToken(), TokenType.REFRESH_TOKEN);
        String userId = claims.getSubject();

        String hash = DigestUtils.sha256Hex(refreshTokenRequest.refreshToken());
        Token stored = tokenRepository.findByToken(hash)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token tidak ditemukan"));

        if (stored.isExpired() || stored.isRevoked()) {
            throw new IllegalArgumentException("Refresh token telah kadaluarsa atau dicabut");
        }

        User user = stored.getUser();
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("Pemilik token tidak cocok");
        }



        stored.revoke();
        tokenRepository.save(stored);

        String newAccessToken = tokenService.generateAccessToken(user.getId());
        TokenPair tokenPair = tokenService.createRefreshToken(stored.getUser());


        tokenRepository.save(tokenPair.refreshToken());

        return new TokenResponse(
                newAccessToken,
                tokenPair.RawToken(),
                tokenService.getTtlAccessToken()
        );

    }
 }
