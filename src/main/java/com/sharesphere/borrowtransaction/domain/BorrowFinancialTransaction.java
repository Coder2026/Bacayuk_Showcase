package com.sharesphere.borrowtransaction.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowFinancialTransaction {
    @Id
    String id;
    BigDecimal dailyRent;
    BigDecimal totalAmount;
    BigDecimal guarantee;
    BigDecimal refundedAmount;
    BigDecimal payoutAmount;
    @OneToOne
    @JoinColumn(name = "borrow_transaction_id")
    BorrowTransaction borrowTransaction;

    @Enumerated(EnumType.STRING)
    BorrowFinancialStatus status;
}
