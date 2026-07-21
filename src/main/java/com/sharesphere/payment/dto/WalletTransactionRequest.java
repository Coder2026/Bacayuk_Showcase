package com.sharesphere.payment.dto;

import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.domain.WalletTransactionType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record WalletTransactionRequest(
        WalletTransactionStatus status,
        WalletTransactionType type,
        String keyword,
        Integer page,
        Integer size
) {
    public Pageable toPageable() {
        int p = (page != null && page >= 0) ? page : 0;
        int s = (size != null && size > 0) ? size : 10;

        return PageRequest.of(p, s, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
