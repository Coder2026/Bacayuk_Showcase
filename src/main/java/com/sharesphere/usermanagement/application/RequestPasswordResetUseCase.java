package com.sharesphere.usermanagement.application;

import com.sharesphere.share.utils.LinkBuilder;
import com.sharesphere.usermanagement.domain.EmailService;
import com.sharesphere.usermanagement.domain.TokenService;
import com.sharesphere.usermanagement.domain.UserRepository;
import com.sharesphere.usermanagement.dto.RequestPasswordResetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestPasswordResetUseCase {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final LinkBuilder linkBuilder;
    private final EmailService emailService;

    public void execute(RequestPasswordResetRequest request) {

        userRepository.findByEmail(request.email()).ifPresent(user -> {

            if (!user.isEmailVerified()) {
                return;
            }


            String resetToken = tokenService.generatePasswordResetToken(user.getId());
            String resetLink  = linkBuilder.passwordResetLink(resetToken);
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    Map.of("name", user.getName(), "resetLink", resetLink)
            );
        });
    }
}
