package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.Review;
import com.sharesphere.borrowtransaction.domain.ReviewRepository;
import com.sharesphere.borrowtransaction.dto.AvgRatingProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public void save(Review review) {
        reviewJpaRepository.save(review);
    }

    @Override
    public List<Review> findByUserAndRole(String userId, String role) {
        return reviewJpaRepository.findByUserAndRole(userId, role);
    }

    @Override
    public AvgRatingProjection findAvgRatingByUser(String userId) {
        return reviewJpaRepository.findAvgRatingsByUser(userId);
    }

    @Override
    public Optional<Review> findByReviewerAndTransaction(String userId, String borrowTransactionId) {
        return reviewJpaRepository.findByReviewerAndTransaction(userId, borrowTransactionId);
    }

    @Override
    public Optional<Review> findById(String id) {
        return reviewJpaRepository.findById(id);
    }
}
