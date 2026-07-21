package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.borrowtransaction.dto.*;
import com.sharesphere.payment.domain.Wallet;
import com.sharesphere.payment.domain.WalletRepository;
import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.share.service.S3Service;
import com.sharesphere.share.utils.GeoFactory;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestBorrowUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final WalletRepository walletRepository;
    private  final BorrowTransactionMapper borrowTransactionMapper;

    @Transactional
    public BorrowDetailResponse execute(String userId, BorrowRequest borrowRequest){

        User borrower = userRepository.getReferenceById(userId);

        Post post = postRepository.findById(borrowRequest.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post tidak ditemukan"));

        if(post.getStatus().name().equals("UNAVAILABLE")){
            throw new IllegalStateException("Post tidak tersedia untuk dipinjam");
        }

        Wallet wallet = walletRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet tidak ditemukan"));

        Point location = GeoFactory.INSTANCE.createPoint(
                new Coordinate(borrowRequest.longitude(), borrowRequest.latitude())
        );

        BorrowStatus borrowStatus = borrowStatusRepository.getReferenceById(1L); // REQUESTED

        BorrowTransaction borrowTransaction = BorrowTransaction.builder()
                .id(UUID.randomUUID().toString())
                .post(post)
                .startDate(borrowRequest.startDate())
                .endDate(borrowRequest.endDate())
                .meetingPoint(location)
                .borrower(borrower)
                .lender(post.getOwner())
                .status(borrowStatus)
                .createdAt(Instant.now())
                .build();

        borrowTransactionRepository.save(borrowTransaction);

        long days = ChronoUnit.DAYS.between(borrowRequest.startDate(), borrowRequest.endDate()) + 1;
        if (days <= 0) {
            throw new IllegalArgumentException("Tanggal akhir harus lebih besar atau sama dengan tanggal mulai");
        }

        BigDecimal rentFee = post.getDailyRentFee();
        BigDecimal guarantee = post.getGuarantee();
        BigDecimal totalAmount = rentFee.multiply(BigDecimal.valueOf(days)).add(guarantee);

        if (wallet.getBalance().compareTo(totalAmount) < 0) {
            throw new IllegalStateException("Saldo tidak mencukupi untuk melakukan peminjaman");
        }

        wallet.deductBalance(totalAmount);
        walletRepository.save(wallet);

        BorrowFinancialTransaction financialTransaction = BorrowFinancialTransaction.builder()
                .id(UUID.randomUUID().toString())
                .dailyRent(rentFee)
                .guarantee(guarantee)
                .totalAmount(totalAmount)
                .borrowTransaction(borrowTransaction)
                .status(BorrowFinancialStatus.ON_HOLD)
                .build();

        borrowFinancialTransactionRepository.save(financialTransaction);

        return borrowTransactionMapper.toDetailResponse(borrowTransaction,userId);
    }
}
