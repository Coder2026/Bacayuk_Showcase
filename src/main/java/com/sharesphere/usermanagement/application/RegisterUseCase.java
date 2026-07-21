package com.sharesphere.usermanagement.application;

import
        com.sharesphere.share.dto.Response;
import com.sharesphere.share.utils.LinkBuilder;
import com.sharesphere.usermanagement.domain.*;
import com.sharesphere.usermanagement.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final UserPasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final LinkBuilder linkBuilder;


    public void execute(RegisterRequest request) {

        passwordValidator.validate(request.password());
        String hashedPassword = passwordEncoder.encode(request.password());

        User user = userRepository.findByEmail(request.email())
                .map(existing -> {
                    if (existing.isEmailVerified()) {
                        throw new IllegalArgumentException("Email sudah terdaftar");
                    }
                    existing.setName(request.name());
                    existing.setPassword(hashedPassword);
                    return existing;
                })
                .orElseGet(() -> User.builder()
                        .id(UUID.randomUUID().toString())
                        .name(request.name())
                        .email(request.email())
                        .password(hashedPassword)
                        .emailVerified(false)
                        .role(Role.USER)
                        .build());

        userRepository.save(user);

        String token = tokenService.generateEmailVerification(user.getId());
        String link = linkBuilder.verificationLink(token);

        emailService.sendVerificationEmail(
                user.getEmail(),
                Map.of("name", user.getName(), "verificationLink", link)
        );

    }

}
