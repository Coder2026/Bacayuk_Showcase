package com.sharesphere.payment.domain;

import com.sharesphere.payment.dto.BankResponse;
import com.sharesphere.payment.dto.BanksResponse;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {


    private final BankRepository bankRepository;
    private final S3Service s3Service;

    public BanksResponse getAllBanks() {

        List<BankResponse> banks = bankRepository.findAll()
                .stream()
                .map(bank -> new BankResponse(
                        bank.getId(),
                        bank.getName(),
                        bank.getCode(),
                        s3Service.getPublicUrlFromKey(bank.getPhotoKey())
                ))
                .toList();

        return new BanksResponse(banks);
    }
}
