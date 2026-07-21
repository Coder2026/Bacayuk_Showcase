package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import com.sharesphere.post.domain.PostStatus;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RejectDisputeUseCase {

    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final BorrowTransactionRepository borrowTransactionRepository;
    private final WalletRepository walletRepository;
    private final DisputeRepository disputeRepository;
    private final BorrowStatusRepository borrowStatusRepository;

    @Transactional
    public void execute(String userId, String disputeId) {


        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new IllegalArgumentException("Dispute tidak ditemukan"));

        BorrowTransaction borrowTransaction = dispute.getBorrowTransaction();

        if (borrowTransaction.getStatus().getId() != 7L) {
            throw new IllegalStateException("Transaksi tidak dalam status dispute");
        }

        BorrowFinancialTransaction financialTx = borrowFinancialTransactionRepository
                .findByBorrowTransaction(borrowTransaction)
                .orElseThrow(() -> new IllegalStateException("Transaksi keuangan tidak ditemukan"));

        BigDecimal totalAmount = financialTx.getTotalAmount();
        BigDecimal guarantee = financialTx.getGuarantee();

        BigDecimal totalDailyRent = totalAmount.subtract(guarantee);

        BigDecimal adminRate = new BigDecimal("0.05"); // 5%
        BigDecimal adminFee = totalDailyRent
                .multiply(adminRate)
                .setScale(0, RoundingMode.DOWN);

        BigDecimal refundAmount = guarantee;
        BigDecimal payoutAmount = totalDailyRent.subtract(adminFee);

        financialTx.setRefundedAmount(refundAmount);
        financialTx.setPayoutAmount(payoutAmount);
        financialTx.setStatus(BorrowFinancialStatus.COMPLETED);
        borrowFinancialTransactionRepository.save(financialTx);


        Wallet borrowerWallet = walletRepository.findByOwnerId(borrowTransaction.getBorrower().getId())
                .orElseThrow(() -> new RuntimeException("Wallet borrower tidak ditemukan"));
        borrowerWallet.addBalance(refundAmount);
        walletRepository.save(borrowerWallet);

        Wallet lenderWallet = walletRepository.findByOwnerId(borrowTransaction.getLender().getId())
                .orElseThrow(() -> new RuntimeException("Wallet lender tidak ditemukan"));
        lenderWallet.addBalance(payoutAmount);
        walletRepository.save(lenderWallet);

        borrowTransaction.getPost().setStatus(PostStatus.AVAILABLE);
        BorrowStatus completedStatus = borrowStatusRepository.getReferenceById(8L);
        borrowTransaction.setStatus(completedStatus);
        borrowTransactionRepository.save(borrowTransaction);

        dispute.setStatus(DisputeStatus.REJECTED);
        dispute.setResolvedBy(userId);
        dispute.setResolvedAt(Instant.now());
        disputeRepository.save(dispute);
    }
}
