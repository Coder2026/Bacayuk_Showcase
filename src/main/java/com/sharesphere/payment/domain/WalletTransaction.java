package com.sharesphere.payment.domain;

import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class WalletTransaction {
    @Id
    String id;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    Wallet wallet;
    BigDecimal amount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletTransactionType type;

    @Column(nullable = false, updatable = false)
    Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "verified_by_id")
    User verifiedBy;
    Instant verifiedAt;

    @Enumerated(EnumType.STRING)
    WalletTransactionStatus status;
}
