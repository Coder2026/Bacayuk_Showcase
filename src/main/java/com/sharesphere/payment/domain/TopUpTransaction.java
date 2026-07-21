package com.sharesphere.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class TopUpTransaction extends  WalletTransaction {
    boolean isConfirmed;
    @ManyToOne
    @JoinColumn(name = "platform_bank_account_id")
    PlatformBankAccount platformBankAccount;
    String proofKey;
}
