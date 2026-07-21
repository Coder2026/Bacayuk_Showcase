package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.Dispute;
import com.sharesphere.borrowtransaction.domain.DisputeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisputeJpaRepository extends JpaRepository<Dispute, String> {

    @Query("""
        SELECT d
        FROM Dispute d
        WHERE (:status IS NULL OR d.status = :status)
          AND (
                :keyword IS NULL
             OR LOWER(d.borrowTransaction.borrower.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(d.borrowTransaction.lender.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(d.borrowTransaction.post.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR d.id = :keyword
             OR d.borrowTransaction.id = :keyword
          )
        ORDER BY d.createdAt DESC
        """)
    Page<Dispute> findByStatusAndKeyword(
            @Param("status") DisputeStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
