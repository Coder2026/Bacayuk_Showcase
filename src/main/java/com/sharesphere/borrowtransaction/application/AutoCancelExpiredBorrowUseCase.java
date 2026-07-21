package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.BorrowStatus;
import com.sharesphere.borrowtransaction.domain.BorrowStatusRepository;
import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import com.sharesphere.post.domain.PostStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoCancelExpiredBorrowUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final BorrowCancellationService borrowCancellationService;
    @Transactional
    public int execute() {

        ZoneId zone = ZoneId.of("Asia/Jakarta");
        Instant midnightToday = LocalDate.now(zone)
                .atStartOfDay(zone)
                .toInstant();

        List<BorrowTransaction> expiredTransactions = borrowTransactionRepository
                .findAllByMeetDateBeforeAndStatusIdIn(
                        midnightToday,
                        List.of(1L, 2L)
                );

        if (expiredTransactions.isEmpty()) {
            log.info("Tidak ada transaksi expired yang perlu ditutup");
            return 0;
        }

        BorrowStatus cancelStatus = borrowStatusRepository.getReferenceById(4L);

        expiredTransactions.forEach(tx -> {
            tx.setStatus(cancelStatus);
            tx.getPost().setStatus(PostStatus.AVAILABLE);
            borrowCancellationService.refundBorrower(tx);
            log.info("Transaksi {} ditutup otomatis (lewat 1 hari dari meetDate) dan dana dikembalikan", tx.getId());
        });

        
        borrowTransactionRepository.saveAll(expiredTransactions);
        log.info("Total {} transaksi berhasil ditutup otomatis", expiredTransactions.size());
        return expiredTransactions.size();
    }
}
