package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import com.sharesphere.borrowtransaction.domain.TransactionRole;
import com.sharesphere.borrowtransaction.dto.*;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBorrowTransactionsUseCase {

    private final BorrowTransactionRepository borrowTransactionRepository;
    private final S3Service s3Service;

    public BorrowListResponse execute(String userId, TransactionRole role){
        List<BorrowTransaction> transactions = borrowTransactionRepository.findByUserAndRole(userId, role.name());

        List<BorrowResponse> items = transactions.stream()
                .map(t -> new BorrowResponse(
                        t.getId(),
                        t.getStartDate(),
                        t.getEndDate(),
                        new UserSummary(
                                t.getBorrower().getId(),
                                t.getBorrower().getName()
                        ),
                        new UserSummary(
                                t.getLender().getId(),
                                t.getLender().getName()
                        ),
                        new PostSummary(
                                t.getPost().getId(),
                                t.getPost().getTitle(),
                                s3Service.getPublicUrlFromKey( t.getPost().getPhotoKey())
                        ),
                        new BorrowStatusSummary(
                                t.getStatus().getId(),
                                t.getStatus().getName())
                        )
                ).toList();

        return new BorrowListResponse(items);
    }
}
