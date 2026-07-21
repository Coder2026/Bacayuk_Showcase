package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.domain.PostStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowTransactionCompleter {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final WalletRepository walletRepository;
    private final PostRepository postRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(BorrowTransaction tx) {
        try {
            // 1️⃣ Update post status
            Post post = tx.getPost();
            post.setStatus(PostStatus.AVAILABLE);
            postRepository.save(post);

            // 2️⃣ Update borrow transaction status menjadi completed
            BorrowStatus completedStatus = borrowStatusRepository.getReferenceById(8L);
            tx.setStatus(completedStatus);
            borrowTransactionRepository.save(tx);

            // 3️⃣ Ambil financial transaction
            BorrowFinancialTransaction financialTransaction = borrowFinancialTransactionRepository
                    .findByBorrowTransaction(tx)
                    .orElseThrow(() -> new IllegalStateException("Transaksi keuangan tidak ditemukan"));

            BigDecimal guarantee = financialTransaction.getGuarantee();
            BigDecimal totalAmount = financialTransaction.getTotalAmount();
            BigDecimal dailyRentFee = financialTransaction.getDailyRent();

            // 4️⃣ Hitung penalty
            long lateDays = ChronoUnit.DAYS.between(tx.getEndDate(), tx.getReturnDate());
            if (lateDays < 0) lateDays = 0;

            BigDecimal penalty = BigDecimal.ZERO;
            if (dailyRentFee != null && dailyRentFee.compareTo(BigDecimal.ZERO) > 0) {
                penalty = dailyRentFee.multiply(BigDecimal.valueOf(lateDays));
            }
            if (penalty.compareTo(guarantee) > 0) penalty = guarantee;

            BigDecimal refundedAmount = guarantee.subtract(penalty);
            BigDecimal payout = totalAmount.subtract(guarantee).add(penalty);

            // 5️⃣ Update financial transaction
            financialTransaction.setRefundedAmount(refundedAmount);
            financialTransaction.setPayoutAmount(payout);
            financialTransaction.setStatus(BorrowFinancialStatus.COMPLETED);
            borrowFinancialTransactionRepository.save(financialTransaction);

            // 6️⃣ Update wallet borrower
            Wallet borrowerWallet = walletRepository.findByOwnerId(tx.getBorrower().getId())
                    .orElseThrow(() -> new RuntimeException("Wallet borrower tidak ditemukan"));
            borrowerWallet.addBalance(refundedAmount);
            walletRepository.save(borrowerWallet);

            // 7️⃣ Update wallet lender
            Wallet lenderWallet = walletRepository.findByOwnerId(tx.getLender().getId())
                    .orElseThrow(() -> new RuntimeException("Wallet lender tidak ditemukan"));
            lenderWallet.addBalance(payout);
            walletRepository.save(lenderWallet);

            log.info("Auto-complete transaksi {} berhasil", tx.getId());

        } catch (Exception e) {
            log.error("Gagal auto-complete transaksi {}: {}", tx.getId(), e.getMessage());
        }
    }
}