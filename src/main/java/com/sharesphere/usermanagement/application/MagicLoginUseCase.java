package com.sharesphere.usermanagement.application;

import com.sharesphere.usermanagement.domain.TokenType;
import com.sharesphere.usermanagement.domain.*;
import com.sharesphere.usermanagement.dto.*;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MagicLoginUseCase {

    private  final TokenService tokenService;
    private  final UserRepository userRepository;
    private  final TokenRepository tokenRepository;

    public LoginResponse execute(MagicLoginRequest magicLoginRequest){

        Claims claims = tokenService.validateToken(magicLoginRequest.oneTimeToken(), TokenType.ONE_TIME_LOGIN);
        String userId = claims.getSubject();

        log.debug("Token validated. Subject (userId): {}", claims.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User tidak ditemukan"));

        String accessToken = tokenService.generateAccessToken(user.getId());
        TokenPair tokenPair = tokenService.createRefreshToken(user);

        tokenRepository.save(tokenPair.refreshToken());

        PrivateUserResponse.Location location = Optional.ofNullable(user.getLocation())
                .map(l -> new PrivateUserResponse.Location(l.getX(), l.getY()))
                .orElse(null);

        return new LoginResponse(new TokenResponse(
                accessToken,
                tokenPair.RawToken(),
                tokenService.getTtlAccessToken()
        ), new PrivateUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhotoKey(),
                location
        ));
    }
}
