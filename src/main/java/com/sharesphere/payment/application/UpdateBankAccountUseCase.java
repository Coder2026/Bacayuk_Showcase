package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.Bank;
import com.sharesphere.payment.domain.BankAccount;
import com.sharesphere.payment.domain.BankAccountRepository;
import com.sharesphere.payment.domain.BankRepository;
import com.sharesphere.payment.dto.BankAccountRequest;
import com.sharesphere.payment.dto.BankAccountResponse;
import com.sharesphere.payment.dto.BankResponse;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBankAccountUseCase {

    private final BankAccountRepository bankAccountRepository;
    private final BankRepository bankRepository;
    private final S3Service s3Service;

    public BankAccountResponse execute(String userId,String bankAccountId, BankAccountRequest request) {


        BankAccount existing = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank Account tidak ditemukan"));

        if (!existing.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Bank Account tidak dimiliki user ini");
        }

        Bank bank = bankRepository.findById(request.bankId())
                .orElseThrow(() -> new IllegalArgumentException("Bank tidak ditemukan"));

        existing.setAccountNumber(request.accountNumber());
        existing.setAccountName(request.accountName());
        existing.setBank(bank);

        bankAccountRepository.save(existing);

        return new BankAccountResponse(
                existing.getId(),
                existing.getAccountNumber(),
                existing.getAccountName(),
                new BankResponse(existing.getBank().getId(),
                        existing.getBank().getName(),
                        existing.getBank().getCode(),
                        s3Service.getPublicUrlFromKey(existing.getBank().getPhotoKey()))
        );
    }
}
