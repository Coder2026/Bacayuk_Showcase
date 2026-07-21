package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.Review;
import com.sharesphere.borrowtransaction.domain.ReviewRepository;
import com.sharesphere.borrowtransaction.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetReviewUseCase {


    private final ReviewRepository reviewRepository;

    public ReviewResponse execute(String reviewId) {

        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review tidak ditemukan"));

        return new ReviewResponse(
                r.getId(),
                r.getComment(),
                r.getRating()
        );
    }
}
