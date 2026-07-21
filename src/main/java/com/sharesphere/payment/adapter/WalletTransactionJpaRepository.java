package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletTransactionJpaRepository extends JpaRepository<WalletTransaction, String> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM TopUpTransaction t " +
            "WHERE t.amount = :amount AND t.status = :status")
    boolean existsTopUpByAmountAndStatus(@Param("amount") BigDecimal amount,
                                         @Param("status") WalletTransactionStatus status);

    @Query("SELECT t FROM WalletTransaction t " +
            "WHERE TYPE(t) = TopUpTransaction " +
            "AND t.wallet.owner.id = :userId " +
            "AND t.status = :status " +
            "ORDER BY t.createdAt DESC")
    Optional<TopUpTransaction> findActiveTopUp(@Param("userId") String userId,
                                               @Param("status") WalletTransactionStatus status);

    @Query("SELECT t FROM WalletTransaction t " +
            "WHERE TYPE(t) = WithdrawalTransaction " +
            "AND t.wallet.owner.id = :userId " +
            "AND t.status = :status " +
            "ORDER BY t.createdAt DESC")
    Optional<WithdrawalTransaction> findActiveWithdrawal(@Param("userId") String userId,
                                                         @Param("status") WalletTransactionStatus status);


    @Query("""
        SELECT wt
        FROM WalletTransaction wt
        JOIN wt.wallet w
        JOIN w.owner u
        WHERE (:status IS NULL OR wt.status = :status)
          AND (:type IS NULL OR wt.type = :type)
          AND (
                :keyword IS NULL
             OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR wt.id = :keyword
          )
        ORDER BY wt.createdAt DESC
        """)
    Page<WalletTransaction> findByFilters(
            @Param("status") WalletTransactionStatus status,
            @Param("type") WalletTransactionType type,
            @Param("keyword") String keyword,
            Pageable pageable
    );


}
