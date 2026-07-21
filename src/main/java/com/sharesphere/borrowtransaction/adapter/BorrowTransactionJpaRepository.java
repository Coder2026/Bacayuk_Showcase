package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BorrowTransactionJpaRepository extends JpaRepository<BorrowTransaction, String> {
    @Query("""
    SELECT t FROM BorrowTransaction t
    WHERE
      (:role = 'BORROWER' AND t.borrower.id = :userId)
      OR (:role = 'LENDER' AND t.lender.id = :userId)
      ORDER BY t.createdAt DESC
""")
    List<BorrowTransaction> findByUserAndRole(@Param("userId") String userId, @Param("role") String role);

    @Query("""
        SELECT bt FROM BorrowTransaction bt
        WHERE bt.startDate < :date
          AND bt.status.id IN :statusIds
    """)
    List<BorrowTransaction> findAllByMeetDateBeforeAndStatusIdIn(
            @Param("date") Instant date,
            @Param("statusIds") List<Long> statusIds
    );

        @Query("""
    SELECT bt FROM BorrowTransaction bt
    WHERE bt.status.id = :statusId
      AND bt.returnDate IS NOT NULL
      AND bt.returnDate < :cutoffDate
    """)
    List<BorrowTransaction> findExpiredTransactions(
            @Param("statusId") Long statusId,
            @Param("cutoffDate") Instant cutoffDate);
}
