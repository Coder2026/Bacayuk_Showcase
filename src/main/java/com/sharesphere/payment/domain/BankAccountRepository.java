package com.sharesphere.payment.domain;

import java.util.Optional;

public interface BankAccountRepository {
    BankAccount save(BankAccount bankAccount);
    Optional<BankAccount> findById(String id);
    Optional<BankAccount> findByOwnerId(String userId);
}
