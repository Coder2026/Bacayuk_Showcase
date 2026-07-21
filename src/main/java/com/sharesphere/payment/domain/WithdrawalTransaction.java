package com.sharesphere.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class WithdrawalTransaction extends WalletTransaction {

    @ManyToOne
    @JoinColumn(name = "bank_id")
    Bank bank;
    String transferProofKey;
    String accountNumber;
    String accountHolderName;
}
