package com.sharesphere.usermanagement.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "refresh_tokens")
public class Token {

    @Id
    private String id;
    @Column(length = 512)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
    private TokenType type;
    private Instant expiresAt;
    private boolean revoked;


    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
