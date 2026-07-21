package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.borrowtransaction.dto.BorrowDetailResponse;
import com.sharesphere.borrowtransaction.dto.BorrowTransactionMapper;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.domain.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class CompleteBorrowUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final WalletRepository walletRepository;
    private final PostRepository postRepository;
    private final BorrowTransactionMapper borrowTransactionMapper;

    @Transactional
    public BorrowDetailResponse execute(String userId, String borrowTransactionId) {
        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        Long statusId = borrowTransaction.getStatus().getId();
        if (statusId != 5L && statusId != 6L) {
            throw new IllegalStateException("Transaksi belum dalam tahap pengembalian");
        }

        Post post = borrowTransaction.getPost();
        post.setStatus(PostStatus.AVAILABLE);
        postRepository.save(post);

        BorrowStatus completedStatus = borrowStatusRepository.getReferenceById(8L);
        borrowTransaction.setStatus(completedStatus);

        Instant effectiveReturnDate = borrowTransaction.getReturnDate();
        if (effectiveReturnDate == null) {
            effectiveReturnDate = Instant.now();
            borrowTransaction.setReturnDate(effectiveReturnDate);
        }
        borrowTransactionRepository.save(borrowTransaction);

        BorrowFinancialTransaction financialTransaction = borrowFinancialTransactionRepository
                .findByBorrowTransaction(borrowTransaction)
                .orElseThrow(() -> new IllegalStateException("Transaksi keuangan tidak ditemukan"));

        BigDecimal guarantee = financialTransaction.getGuarantee();
        BigDecimal totalAmount = financialTransaction.getTotalAmount();
        BigDecimal dailyRentFee = financialTransaction.getDailyRent();


        Instant endDate = borrowTransaction.getEndDate();
        long lateDays = ChronoUnit.DAYS.between(endDate, effectiveReturnDate);
        if (lateDays < 0) lateDays = 0;

        BigDecimal penalty = BigDecimal.ZERO;
        if (dailyRentFee != null && dailyRentFee.compareTo(BigDecimal.ZERO) > 0) {
            penalty = dailyRentFee.multiply(BigDecimal.valueOf(lateDays));
        }

        if (penalty.compareTo(guarantee) > 0) {
            penalty = guarantee;
        }

        // 9. Hitung total sewa & admin fee
        BigDecimal totalDailyRent = totalAmount.subtract(guarantee);

        BigDecimal adminRate = new BigDecimal("0.05"); // 5%
        BigDecimal adminFee = totalDailyRent
                .multiply(adminRate)
                .setScale(0, RoundingMode.DOWN);

        BigDecimal netRentToLender = totalDailyRent.subtract(adminFee);

        // 10. Final settlement
        BigDecimal refundedAmount = guarantee.subtract(penalty);
        BigDecimal payout = netRentToLender.add(penalty);

        financialTransaction.setRefundedAmount(refundedAmount);
        financialTransaction.setPayoutAmount(payout);
        financialTransaction.setStatus(BorrowFinancialStatus.COMPLETED);
        borrowFinancialTransactionRepository.save(financialTransaction);

        Wallet borrowerWallet = walletRepository.findByOwnerId(borrowTransaction.getBorrower().getId())
                .orElseThrow(() -> new RuntimeException("Wallet borrower tidak ditemukan"));
        borrowerWallet.addBalance(refundedAmount);
        walletRepository.save(borrowerWallet);

        Wallet lenderWallet = walletRepository.findByOwnerId(borrowTransaction.getLender().getId())
                .orElseThrow(() -> new RuntimeException("Wallet lender tidak ditemukan"));
        lenderWallet.addBalance(payout);
        walletRepository.save(lenderWallet);

        return borrowTransactionMapper.toDetailResponse(borrowTransaction, userId);
    }
}