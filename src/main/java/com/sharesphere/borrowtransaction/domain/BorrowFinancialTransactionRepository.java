package com.sharesphere.borrowtransaction.domain;

import java.util.Optional;

public interface BorrowFinancialTransactionRepository {
    void save(BorrowFinancialTransaction borrowFinancialTransaction);
    Optional<BorrowFinancialTransaction> findById(String id);
    Optional<BorrowFinancialTransaction> findByBorrowTransaction(BorrowTransaction borrowTransaction);
}
