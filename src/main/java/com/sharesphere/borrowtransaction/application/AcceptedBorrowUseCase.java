package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.borrowtransaction.dto.BorrowDetailResponse;
import com.sharesphere.borrowtransaction.dto.BorrowTransactionMapper;
import com.sharesphere.post.domain.Post;
import com.sharesphere.post.domain.PostRepository;
import com.sharesphere.post.domain.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AcceptedBorrowUseCase {
    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BorrowStatusRepository borrowStatusRepository;
    private final BorrowTransactionMapper borrowTransactionMapper;
    private final PostRepository postRepository;


    @Transactional
    public BorrowDetailResponse execute(String userId,String borrowTransactionId) {

        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaksi tidak ditemukan"));

        if (borrowTransaction.getStatus().getId() != 1) {
            throw new IllegalStateException("Transaksi tidak dapat diproses karena status tidak valid");
        }


        Post post = borrowTransaction.getPost();


        if (post.getStatus() != PostStatus.AVAILABLE) {
            throw new IllegalStateException("Buku sudah tidak tersedia untuk dipinjamkan.");
        }



        post.setStatus(PostStatus.UNAVAILABLE);
        postRepository.save(post);

        borrowTransaction.setStatus(borrowStatusRepository.getReferenceById(2L));
        borrowTransactionRepository.save(borrowTransaction);

        return borrowTransactionMapper.toDetailResponse(borrowTransaction,userId);
    }
}
