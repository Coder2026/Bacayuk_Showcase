package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountJpaRepository extends JpaRepository<BankAccount,String> {

    @Query("SELECT b FROM BankAccount b WHERE b.user.id = :userId")
    Optional<BankAccount> findByOwnerId(@Param("userId") String userId);
}
