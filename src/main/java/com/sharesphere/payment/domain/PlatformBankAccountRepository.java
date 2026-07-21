package com.sharesphere.payment.domain;

import java.util.Optional;

public interface PlatformBankAccountRepository {
    PlatformBankAccount save(PlatformBankAccount platformBankAccount);
    Optional<PlatformBankAccount> findById(Long id);
    PlatformBankAccount findByBankId(Long bankId);
}
