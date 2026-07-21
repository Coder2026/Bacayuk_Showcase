package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.BorrowTransaction;
import com.sharesphere.borrowtransaction.domain.BorrowTransactionRepository;
import com.sharesphere.borrowtransaction.domain.Review;
import com.sharesphere.borrowtransaction.domain.ReviewRepository;
import com.sharesphere.borrowtransaction.dto.ReviewRequest;
import com.sharesphere.usermanagement.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final BorrowTransactionRepository borrowTransactionRepository;

    @Transactional
    public void execute(String userId,String borrowTransactionId, ReviewRequest request){

        BorrowTransaction transaction = borrowTransactionRepository.findById(borrowTransactionId)
                .orElseThrow(() -> new RuntimeException("Borrow transaction not found"));

        User reviewer;
        User reviewee;

        if (transaction.getBorrower().getId().equals(userId)) {
            reviewer = transaction.getBorrower();
            reviewee = transaction.getLender();
        } else if (transaction.getLender().getId().equals(userId)) {
            reviewer = transaction.getLender();
            reviewee = transaction.getBorrower();
        } else {
            throw new IllegalArgumentException("User is not part of this transaction");
        }

        Review review = Review.builder()
                .id(UUID.randomUUID().toString())
                .borrowTransaction(transaction)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .comment(request.comment())
                .rating(request.rating())
                .build();

        reviewRepository.save(review);
    }
}
