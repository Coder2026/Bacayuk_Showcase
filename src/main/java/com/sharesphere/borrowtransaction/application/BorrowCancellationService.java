package com.sharesphere.borrowtransaction.application;
import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BorrowCancellationService {

    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void refundBorrower(BorrowTransaction borrowTransaction) {
        BorrowFinancialTransaction borrowFinancialTransaction = borrowFinancialTransactionRepository
                .findByBorrowTransaction(borrowTransaction)
                .orElseThrow(() -> new IllegalStateException("Transaksi keuangan tidak ditemukan"));


        borrowFinancialTransaction.setRefundedAmount(borrowFinancialTransaction.getTotalAmount());
        borrowFinancialTransaction.setPayoutAmount(BigDecimal.ZERO);
        borrowFinancialTransaction.setStatus(BorrowFinancialStatus.REFUNDED);


        Wallet wallet = walletRepository.findByOwnerId(borrowTransaction.getBorrower().getId())
                .orElseThrow(() -> new IllegalStateException("Wallet tidak ditemukan"));

        wallet.addBalance(borrowFinancialTransaction.getRefundedAmount());
    }
}
