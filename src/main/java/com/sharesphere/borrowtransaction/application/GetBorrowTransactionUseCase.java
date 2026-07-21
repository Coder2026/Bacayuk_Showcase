package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import com.sharesphere.borrowtransaction.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBorrowTransactionUseCase {


    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowTransactionMapper borrowTransactionMapper;

    public BorrowDetailResponse execute(String userId,String borrowTransactionId) {
        BorrowTransaction t = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        return borrowTransactionMapper.toDetailResponse(t,userId);
    }
}
