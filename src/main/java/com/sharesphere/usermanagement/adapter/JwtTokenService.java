package com.sharesphere.usermanagement.adapter;

import com.sharesphere.usermanagement.config.JwtProperties;
import com.sharesphere.usermanagement.domain.Token;
import com.sharesphere.usermanagement.domain.TokenService;
import com.sharesphere.usermanagement.domain.TokenType;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.dto.TokenPair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService implements TokenService {

    private final JwtProperties cfg;
    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = Keys.hmacShaKeyFor(
                cfg.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateEmailVerification(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("token_type", TokenType.EMAIL_VERIFICATION.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now
                        .plus(cfg.getEmail().getExpDays(), ChronoUnit.DAYS)
                        .plus(cfg.getEmail().getExpHours(), ChronoUnit.HOURS)
                        .plus(cfg.getEmail().getExpMinutes(), ChronoUnit.MINUTES)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateOneTimeTokenLogin(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("token_type", TokenType.ONE_TIME_LOGIN.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now
                        .plus(cfg.getOnetime().getExpDays(), ChronoUnit.DAYS)
                        .plus(cfg.getOnetime().getExpHours(), ChronoUnit.HOURS)
                        .plus(cfg.getOnetime().getExpMinutes(), ChronoUnit.MINUTES)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generatePasswordResetToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("token_type", TokenType.PASSWORD_RESET.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now
                        .plus(cfg.getPasswordReset().getExpDays(), ChronoUnit.DAYS)
                        .plus(cfg.getPasswordReset().getExpHours(), ChronoUnit.HOURS)
                        .plus(cfg.getPasswordReset().getExpMinutes(), ChronoUnit.MINUTES)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateAccessToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .claim("token_type", TokenType.ACCESS_TOKEN.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now
                        .plus(cfg.getAccess().getExpDays(), ChronoUnit.DAYS)
                        .plus(cfg.getAccess().getExpHours(), ChronoUnit.HOURS)
                        .plus(cfg.getAccess().getExpMinutes(), ChronoUnit.MINUTES)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public TokenPair createRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expireAt = now
                .plus(cfg.getRefresh().getExpDays(), ChronoUnit.DAYS)
                .plus(cfg.getRefresh().getExpHours(), ChronoUnit.HOURS)
                .plus(cfg.getRefresh().getExpMinutes(), ChronoUnit.MINUTES);

        String raw = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(user.getId())
                .claim("jti", UUID.randomUUID().toString())
                .claim("token_type", TokenType.REFRESH_TOKEN.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expireAt))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        String hashed = DigestUtils.sha256Hex(raw);

        return new TokenPair(new Token(
                UUID.randomUUID().toString(),
                hashed,
                user,
                TokenType.REFRESH_TOKEN,
                expireAt,
                false
        ), raw);
    }

    @Override
    public long getTtlAccessToken() {
        return cfg.getAccess().getExpDays()    * 24 * 60 * 60L +
                cfg.getAccess().getExpHours()   * 60 * 60L +
                cfg.getAccess().getExpMinutes() * 60L;
    }

    @Override
    public Claims validateToken(String token, TokenType tokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String typeInToken = claims.get("token_type", String.class);
            if (!tokenType.name().equals(typeInToken)) {
                log.warn("Token type mismatch: expected={}, found={}", tokenType.name(), typeInToken);
                throw new JwtException("Invalid token type: expected " + tokenType.name() + " but got " + typeInToken);
            }

            return claims;
        } catch (ExpiredJwtException e) {
            log.warn("Token expired for subject={}, type={}", e.getClaims().getSubject(), tokenType.name());
            throw new JwtException("Token expired");
        } catch (JwtException e) {
            log.error("Invalid token for type={}: {}", tokenType.name(), e.getMessage());
            throw new JwtException("Invalid token");
        }
    }

}