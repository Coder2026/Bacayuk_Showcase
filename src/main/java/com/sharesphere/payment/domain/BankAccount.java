package com.sharesphere.payment.domain;

import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BankAccount {
    @Id
    String Id;
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
    String accountNumber;
    String accountName;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    Bank bank;
}
