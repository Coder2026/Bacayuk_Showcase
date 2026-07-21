package com.sharesphere.payment.application;

import com.sharesphere.payment.domain.WalletTransactionRepository;
import com.sharesphere.payment.dto.WalletTransactionRequest;
import com.sharesphere.payment.dto.WalletTransactionSummary;
import com.sharesphere.payment.dto.WalletTransactionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWalletTransactionsUseCase {

    private final WalletTransactionRepository repository;

    public WalletTransactionsResponse execute(WalletTransactionRequest request) {
        var page = repository.findByFilters(
                request.status(),
                request.type(),
                request.keyword(),
                request.toPageable()
        );


        List<WalletTransactionSummary> summaries = page.stream()
                .map(tx -> new WalletTransactionSummary(
                        tx.getId(),
                        tx.getType().name(),
                        tx.getWallet().getOwner().getName(),
                        tx.getAmount(),
                        tx.getStatus(),
                        tx.getCreatedAt()
                ))
                .toList();


        return new WalletTransactionsResponse(
                summaries,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
}
