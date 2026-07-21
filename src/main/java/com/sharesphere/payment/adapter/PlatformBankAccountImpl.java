package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.PlatformBankAccount;
import com.sharesphere.payment.domain.PlatformBankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlatformBankAccountImpl implements PlatformBankAccountRepository {

    private  final PlatformBankAccountJpaRepository platformBankAccountJpaRepository;

    @Override
    public PlatformBankAccount save(PlatformBankAccount platformBankAccount) {
        return platformBankAccountJpaRepository.save(platformBankAccount);
    }

    @Override
    public Optional<PlatformBankAccount> findById(Long id) {
        return platformBankAccountJpaRepository.findById(id);
    }

    @Override
    public PlatformBankAccount findByBankId(Long bankId) {
        return platformBankAccountJpaRepository.findByBank_Id(bankId);
    }


}
