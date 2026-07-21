package com.sharesphere.borrowtransaction.domain;

import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BorrowTransactionRepository {
    void save(BorrowTransaction borrowTransaction);
    Optional<BorrowTransaction> findById(String id);
    List<BorrowTransaction> findByUserAndRole(String userId, String role);
    List<BorrowTransaction> findAllByMeetDateBeforeAndStatusIdIn(
            Instant date,
            List<Long> statusIds
    );

    List<BorrowTransaction> findExpiredTransactions(
            Long statusId,
          Instant cutoffDate);
    void  saveAll(List<BorrowTransaction> borrowTransactions);
}
