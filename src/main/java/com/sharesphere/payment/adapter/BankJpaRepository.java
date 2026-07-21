package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankJpaRepository extends JpaRepository<Bank,Long> {

}
