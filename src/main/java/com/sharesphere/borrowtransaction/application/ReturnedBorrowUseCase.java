package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.BorrowStatus;
import com.sharesphere.borrowtransaction.domain.BorrowStatusRepository;
import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import com.sharesphere.borrowtransaction.dto.BorrowDetailResponse;
import com.sharesphere.borrowtransaction.dto.BorrowTransactionMapper;
import com.sharesphere.borrowtransaction.dto.ConfirmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReturnedBorrowUseCase {


    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private  final BorrowTransactionMapper borrowTransactionMapper;

    @Transactional
    public BorrowDetailResponse execute(String userId,String borrowTransactionId, ConfirmRequest confirmRequest) {

        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));


        if (borrowTransaction.getStatus().getId() != 5) {
            throw new IllegalStateException("Transaksi tidak dapat diproses");
        }

        if (confirmRequest.photoKeys() != null && !confirmRequest.photoKeys().isEmpty()) {
            if (borrowTransaction.getProofReturnedKeys() == null) {
                borrowTransaction.setProofReturnedKeys(new ArrayList<>());
            }
            borrowTransaction.getProofReturnedKeys().addAll(confirmRequest.photoKeys());
        }

        borrowTransaction.setReturnNotes(confirmRequest.notes());
        borrowTransaction.setReturnDate(Instant.now());

        BorrowStatus returnedStatus = borrowStatusRepository.getReferenceById(6L);
        borrowTransaction.setStatus(returnedStatus);

        borrowTransactionRepository.save(borrowTransaction);

        return borrowTransactionMapper.toDetailResponse(borrowTransaction,userId);
    }
}
