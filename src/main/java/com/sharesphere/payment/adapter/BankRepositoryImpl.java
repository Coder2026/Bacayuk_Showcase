package com.sharesphere.payment.adapter;

import com.sharesphere.payment.domain.Bank;
import com.sharesphere.payment.domain.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BankRepositoryImpl implements BankRepository {

    private  final BankJpaRepository bankJpaRepository;
    @Override
    public List<Bank> findAll() {
        return bankJpaRepository.findAll();
    }

    @Override
    public Optional<Bank> findById(Long id) {
        return bankJpaRepository.findById(id);
    }
}
