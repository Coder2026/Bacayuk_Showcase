package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.BorrowFinancialTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowFinancialTransactionRepository;
import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BorrowFinancialTransactionRepositoryImpl implements BorrowFinancialTransactionRepository {
    private  final BorrowFinancialTransactionJpaRepository borrowFinancialTransactionJpaRepository;

    @Override
    public void save(BorrowFinancialTransaction borrowFinancialTransaction) {
      borrowFinancialTransactionJpaRepository.save(borrowFinancialTransaction);
    }

    @Override
    public Optional<BorrowFinancialTransaction> findById(String id) {
        return borrowFinancialTransactionJpaRepository.findById(id);
    }

    @Override
    public Optional<BorrowFinancialTransaction> findByBorrowTransaction(BorrowTransaction borrowTransaction) {
        return borrowFinancialTransactionJpaRepository.findByBorrowTransaction(borrowTransaction);
    }

}
