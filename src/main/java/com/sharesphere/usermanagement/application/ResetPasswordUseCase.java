package com.sharesphere.usermanagement.application;

import com.sharesphere.usermanagement.domain.*;
import com.sharesphere.usermanagement.dto.ResetPasswordRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final TokenService tokenService;
    private final UserPasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void execute(ResetPasswordRequest request) {

        passwordValidator.validate(request.newPassword());

        Claims claims = tokenService.validateToken(request.token(), TokenType.PASSWORD_RESET);
        String userId = claims.getSubject();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        String hashedPassword = passwordEncoder.encode(request.newPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }
}
