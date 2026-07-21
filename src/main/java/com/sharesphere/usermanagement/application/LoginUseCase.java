package com.sharesphere.usermanagement.application;

import com.sharesphere.usermanagement.domain.*;
import com.sharesphere.usermanagement.dto.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    public LoginResponse execute(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("Email atau password salah"));

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email belum diverifikasi");
        }

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("Email atau password salah");
        }

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
