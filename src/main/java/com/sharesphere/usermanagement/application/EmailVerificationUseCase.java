package com.sharesphere.usermanagement.application;


import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import com.sharesphere.share.exception.WebRedirectException;
import com.sharesphere.usermanagement.domain.TokenType;
import com.sharesphere.share.utils.LinkBuilder;
import com.sharesphere.usermanagement.domain.TokenService;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationUseCase {

    final private TokenService tokenService;
    final private UserRepository userRepository;
    final private WalletRepository walletRepository;
    final private LinkBuilder linkBuilder;
    public String execute(String verificationToken){
        
        Claims claims = tokenService.validateToken(verificationToken,TokenType.EMAIL_VERIFICATION);
        String userId = claims.getSubject();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WebRedirectException("verification-result", "User tidak ditemukan"));

        if (user.isEmailVerified()) {
            throw new WebRedirectException("verification-result", "Email sudah terverifikasi");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
        Wallet wallet = new Wallet(UUID.randomUUID().toString(),user,new BigDecimal(0));
        walletRepository.save(wallet);
        String oneTimeToken  = tokenService.generateOneTimeTokenLogin(userId);
        return linkBuilder.buildMagicLoginUrl(oneTimeToken);
    }

}
