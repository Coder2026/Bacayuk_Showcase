package com.sharesphere.borrowtransaction.adapter;


import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BorrowTransactionRepositoryImpl implements BorrowTransactionRepository {

    private final BorrowTransactionJpaRepository borrowTransactionJpaRepository;
    @Override
    public void save(BorrowTransaction borrowTransaction) {
        borrowTransactionJpaRepository.save(borrowTransaction);
    }

    @Override
    public Optional<BorrowTransaction> findById(String id) {
        return borrowTransactionJpaRepository.findById(id);
    }

    @Override
    public List<BorrowTransaction> findByUserAndRole(String userId, String role) {
        return borrowTransactionJpaRepository.findByUserAndRole(userId, role);
    }

    @Override
    public List<BorrowTransaction> findAllByMeetDateBeforeAndStatusIdIn(Instant date, List<Long> statusIds) {
        return borrowTransactionJpaRepository.findAllByMeetDateBeforeAndStatusIdIn(date, statusIds);
    }

    @Override
    public List<BorrowTransaction> findExpiredTransactions(Long statusId, Instant cutoffDate) {
        return borrowTransactionJpaRepository.findExpiredTransactions(statusId, cutoffDate);
    }

    @Override
    public void saveAll(List<BorrowTransaction> borrowTransactions) {
        borrowTransactionJpaRepository.saveAll(borrowTransactions);
    }



}
