package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.BankAccount;
import com.sharesphere.payment.domain.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final BankAccountJpaRepository bankAccountJpaRepository;

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountJpaRepository.save(bankAccount);
    }


    @Override
    public Optional<BankAccount> findById(String bankAccountId) {
        return bankAccountJpaRepository.findById(bankAccountId);
    }

    @Override
    public Optional<BankAccount> findByOwnerId(String userId) {
        return bankAccountJpaRepository.findByOwnerId(userId);
    }
}
