package com.sharesphere.borrowtransaction.application;


import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.borrowtransaction.dto.BorrowDetailResponse;
import com.sharesphere.borrowtransaction.dto.BorrowTransactionMapper;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import com.sharesphere.post.domain.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CancelBorrowUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final BorrowCancellationService borrowCancellationService;
    private final BorrowTransactionMapper borrowTransactionMapper;

    @Transactional
    public BorrowDetailResponse execute(String userId, String borrowTransactionId) {

        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));


        Long statusId = borrowTransaction.getStatus().getId();
        if (statusId != 1L && statusId != 2L) {
            throw new IllegalStateException("Transaksi tidak dapat dibatalkan");
        }
        borrowTransaction.setStatus(borrowStatusRepository.getReferenceById(4L));
        borrowTransaction.getPost().setStatus(PostStatus.AVAILABLE);
        borrowCancellationService.refundBorrower(borrowTransaction);
        return borrowTransactionMapper.toDetailResponse(borrowTransaction,userId);
    }
}
