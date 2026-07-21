package com.sharesphere.payment.domain;

import com.sharesphere.share.exception.InsufficientBalanceException;
import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Wallet {
    @Id
    private String Id;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private BigDecimal balance;

    public void addBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Nominal tidak boleh lebih kecil dari nol");
        }
        this.balance = this.balance.add(amount);
    }

    public void deductBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Nominal tidak boleh lebih kecil dari nol");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo tidak menucukupi");
        }
        this.balance = this.balance.subtract(amount);
    }
}
