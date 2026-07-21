package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.PlatformBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformBankAccountJpaRepository extends JpaRepository<PlatformBankAccount, Long> {
    PlatformBankAccount findByBank_Id(Long bankId);
}


