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

@Service
@RequiredArgsConstructor
public class ConfirmHandoverUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final BorrowTransactionMapper borrowTransactionMapper;

    @Transactional
    public BorrowDetailResponse execute(String userId,String borrowTransactionId, ConfirmRequest confirmRequest){

        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        if (borrowTransaction.getStatus().getId() != 2) {
            throw new IllegalStateException("Transaksi tidak dapat diproses");
        }

        if (confirmRequest.photoKeys() != null && !confirmRequest.photoKeys().isEmpty()) {
            if (borrowTransaction.getProofHandedOverKeys() == null) {
                // should be initialized by entity, but guard just in case
                borrowTransaction.setProofHandedOverKeys(new java.util.ArrayList<>());
            }
            borrowTransaction.getProofHandedOverKeys().addAll(confirmRequest.photoKeys());
        }

        borrowTransaction.setHandoverNotes(confirmRequest.notes());
        BorrowStatus onLoanStatus = borrowStatusRepository.getReferenceById(5L);
        borrowTransaction.setStatus(onLoanStatus);

        borrowTransactionRepository.save(borrowTransaction);

        return borrowTransactionMapper.toDetailResponse(borrowTransaction,userId);
    }
}
