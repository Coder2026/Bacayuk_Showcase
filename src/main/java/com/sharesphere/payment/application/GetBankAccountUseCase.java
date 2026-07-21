package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.BankAccountRepository;
import com.sharesphere.payment.dto.BankAccountResponse;
import com.sharesphere.payment.dto.BankResponse;
import com.sharesphere.share.exception.BankAccountNotFoundException;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBankAccountUseCase {

    private final BankAccountRepository bankAccountRepository;
    private final S3Service s3Service;
    public BankAccountResponse execute(String userId){

        return bankAccountRepository.findByOwnerId(userId)
                .map(bankAccount -> new BankAccountResponse(
                        bankAccount.getId(),
                        bankAccount.getAccountNumber(),
                        bankAccount.getAccountName(),
                        new BankResponse(bankAccount.getBank().getId(),
                                bankAccount.getBank().getName(),
                                bankAccount.getBank().getCode(),
                                s3Service.getPublicUrlFromKey(bankAccount.getBank().getPhotoKey()))
                )).orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
    }
}
