package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.Review;
import com.sharesphere.borrowtransaction.dto.AvgRatingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ReviewJpaRepository extends JpaRepository<Review, String> {

    @Query("""
        SELECT r FROM Review r
        WHERE r.reviewee.id = :userId
          AND (
              (:role = 'BORROWER' AND r.borrowTransaction.borrower.id = :userId)
              OR (:role = 'LENDER' AND r.borrowTransaction.lender.id = :userId)
          )
        """)
    List<Review> findByUserAndRole(@Param("userId") String userId, @Param("role") String role);

    @Query("""
    SELECT
        AVG(CASE WHEN r.borrowTransaction.borrower.id = :userId THEN r.rating END) AS borrowerAvg,
        AVG(CASE WHEN r.borrowTransaction.lender.id = :userId THEN r.rating END) AS lenderAvg
    FROM Review r
    WHERE r.reviewee.id = :userId
    """)
    AvgRatingProjection findAvgRatingsByUser(@Param("userId") String userId);

    @Query("""
    SELECT r FROM Review r
    WHERE r.reviewer.id = :userId
      AND r.borrowTransaction.id = :borrowTransactionId
""")
    Optional<Review> findByReviewerAndTransaction(
            @Param("userId") String userId,
            @Param("borrowTransactionId") String borrowTransactionId
    );
}
