package com.sharesphere.payment.domain;

import java.util.List;
import java.util.Optional;

public interface BankRepository {
    List<Bank> findAll();
    Optional<Bank> findById(Long id);
}
