package com.sharesphere.usermanagement.adapter;

import com.sharesphere.share.dto.Response;
import com.sharesphere.usermanagement.application.*;
import com.sharesphere.usermanagement.dto.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@SecurityRequirements()
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RegisterUseCase registerUseCase;
    private final EmailVerificationUseCase emailVerificationUseCase;
    private final LoginUseCase loginUseCase;
    private final MagicLoginUseCase magicLoginUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;


    @PostMapping("/refresh")
    public ResponseEntity<Response<TokenResponse>>  refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        TokenResponse response = refreshTokenUseCase.execute(refreshTokenRequest);
        return ResponseEntity.ok( new Response<>("Token berhasil diperbarui", response));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginUseCase.execute(loginRequest);
        return ResponseEntity.ok( new Response<>("Login berhasil", loginResponse));
    }
    @PostMapping("/register")
    public ResponseEntity<Response<String>> register (@RequestBody @Valid RegisterRequest registerRequest){
        registerUseCase.execute(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>("Pendaftaran berhasil. Silakan cek email Anda untuk memverifikasi akun", null));
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Response<String>> requestPasswordReset(@RequestBody @Valid RequestPasswordResetRequest requestPasswordResetRequest) {
        requestPasswordResetUseCase.execute(requestPasswordResetRequest);
        return ResponseEntity.ok(new Response<>("Permintaan reset password berhasil. Silakan cek email Anda untuk tautan reset password", null));
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Response<String>> resetPassword(
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        resetPasswordUseCase.execute(resetPasswordRequest);
        return ResponseEntity.ok(new Response<>("Password berhasil direset", null));
    }

    @GetMapping("/verify")
    public ResponseEntity<Response<String>> verifyEmail (@RequestParam("token") @NotEmpty String verificationToken ){

        String redirectUrl = emailVerificationUseCase.execute(verificationToken);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION,redirectUrl)
                .build();
    }
    @PostMapping("/magic-login")
    public ResponseEntity<Response<LoginResponse>>  magicLogin(@RequestBody @Valid MagicLoginRequest magicLoginRequest) {
        log.debug("📝 Received magic login request: {}", magicLoginRequest.oneTimeToken());
        LoginResponse loginResponse = magicLoginUseCase.execute(magicLoginRequest);
        log.debug("✅ Magic login successful for user: {}", loginResponse.user().id());
        return ResponseEntity.ok(new Response<>("Login berhasil",loginResponse));
    }


    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}


