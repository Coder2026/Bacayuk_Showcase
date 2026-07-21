package com.sharesphere.payment.adapter;


import com.sharesphere.payment.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<Wallet, String> {

    @Query("SELECT w FROM Wallet w WHERE w.owner.id = :ownerId")
    Optional<Wallet> findByOwnerId(@Param("ownerId")String ownerId);

}
