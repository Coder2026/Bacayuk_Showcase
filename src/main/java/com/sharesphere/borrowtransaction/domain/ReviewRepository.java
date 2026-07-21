package com.sharesphere.borrowtransaction.domain;

import com.sharesphere.borrowtransaction.dto.AvgRatingProjection;
import com.sharesphere.usermanagement.domain.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    void save(Review review);
    List<Review> findByUserAndRole(String userId, String role);
    AvgRatingProjection findAvgRatingByUser(String userId);
    Optional<Review> findByReviewerAndTransaction(String userId,String borrowTransactionId);
    Optional<Review> findById(String id);
}
