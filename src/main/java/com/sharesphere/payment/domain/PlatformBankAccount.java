package com.sharesphere.payment.domain;


import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PlatformBankAccount {
    @Id
    Long id;
    String accountNumber;
    String accountName;
    @OneToOne
    @JoinColumn(name = "bank_id")
    Bank bank;
}
