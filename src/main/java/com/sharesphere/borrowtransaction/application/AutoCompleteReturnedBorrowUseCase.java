package com.sharesphere.borrowtransaction.application;


import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoCompleteReturnedBorrowUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowTransactionCompleter borrowTransactionCompleter;

    public int execute() {
        Instant threeDaysAgo = Instant.now().minus(3, ChronoUnit.DAYS);

        List<BorrowTransaction> transactions = borrowTransactionRepository.findExpiredTransactions(6L, threeDaysAgo);

        for (BorrowTransaction tx : transactions) {
            borrowTransactionCompleter.process(tx);
        }

        log.info("Total {} transaksi auto-complete H+3 diproses", transactions.size());
        return transactions.size();
    }
}