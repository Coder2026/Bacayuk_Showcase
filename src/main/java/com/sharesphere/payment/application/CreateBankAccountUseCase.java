package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.Bank;
import com.sharesphere.payment.domain.BankAccount;
import com.sharesphere.payment.domain.BankAccountRepository;
import com.sharesphere.payment.domain.BankRepository;
import com.sharesphere.payment.dto.BankAccountRequest;
import com.sharesphere.payment.dto.BankAccountResponse;
import com.sharesphere.payment.dto.BankResponse;
import com.sharesphere.share.service.S3Service;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBankAccountUseCase {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private  final S3Service s3Service;

    public BankAccountResponse execute(String userId,BankAccountRequest bankAccountRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        Bank bank = bankRepository.findById(bankAccountRequest.bankId())
                .orElseThrow(() -> new IllegalArgumentException("Bank not found with id: " + bankAccountRequest.bankId()));


        BankAccount bankAccount = new BankAccount(
                UUID.randomUUID().toString(),
                user,
                bankAccountRequest.accountNumber(),
                bankAccountRequest.accountName(),
                bank
        );

        bankAccountRepository.save(bankAccount);

        return new BankAccountResponse(
                bankAccount.getId(),
                bankAccount.getAccountNumber(),
                bankAccount.getAccountName(),
                new BankResponse(bankAccount.getBank().getId(),
                        bankAccount.getBank().getName(),
                        bankAccount.getBank().getCode(),
                        s3Service.getPublicUrlFromKey(bankAccount.getBank().getPhotoKey()))
        );
    }
}
