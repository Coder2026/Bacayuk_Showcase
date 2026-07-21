package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.BorrowFinancialTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowFinancialTransactionJpaRepository extends JpaRepository<BorrowFinancialTransaction, String> {
    Optional<BorrowFinancialTransaction> findByBorrowTransaction(BorrowTransaction borrowTransaction);
}
