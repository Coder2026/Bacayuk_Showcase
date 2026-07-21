package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.borrowtransaction.dto.BorrowDetailResponse;
import com.sharesphere.borrowtransaction.dto.BorrowTransactionMapper;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class
RejectBorrowUseCase {
    final private BorrowTransactionRepository borrowTransactionRepository;
    final private BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    final private BorrowStatusRepository borrowStatusRepository;
    final private WalletRepository walletRepository;
    final private BorrowTransactionMapper borrowTransactionMapper;



    @Transactional
    public BorrowDetailResponse execute(String userId, String borrowTransactionId){

        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        if (borrowTransaction.getStatus().getId() != 1) {
            throw new IllegalStateException("Transaksi tidak dapat diproses");
        }


        borrowTransaction.setStatus(borrowStatusRepository.getReferenceById(3L));

        BorrowFinancialTransaction borrowFinancialTransaction = borrowFinancialTransactionRepository
                .findByBorrowTransaction(borrowTransaction)
                .orElseThrow(() -> new IllegalStateException("Transaksi tidak ditemukan"));

        borrowFinancialTransaction.setRefundedAmount(borrowFinancialTransaction.getTotalAmount());
        borrowFinancialTransaction.setPayoutAmount(BigDecimal.ZERO);
        borrowFinancialTransaction.setStatus(BorrowFinancialStatus.REFUNDED);

        // Refund ke wallet borrower
        Wallet wallet = walletRepository.findByOwnerId(borrowTransaction.getBorrower().getId())
                .orElseThrow(() -> new IllegalStateException("Wallet tidak ditemukan"));
        wallet.addBalance(borrowFinancialTransaction.getRefundedAmount());

        return borrowTransactionMapper.toDetailResponse(borrowTransaction,userId);
    }
}
