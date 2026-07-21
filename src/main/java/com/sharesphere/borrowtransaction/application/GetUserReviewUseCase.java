package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.Review;
import com.sharesphere.borrowtransaction.domain.ReviewRepository;
import com.sharesphere.borrowtransaction.domain.TransactionRole;
import com.sharesphere.borrowtransaction.dto.UserReviewItemResponse;
import com.sharesphere.borrowtransaction.dto.UserReviewsResponse;
import com.sharesphere.borrowtransaction.dto.UserProfileSummary;
import com.sharesphere.share.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    public UserReviewsResponse execute(String userId, TransactionRole role){

        List<Review> reviews = reviewRepository.findByUserAndRole(userId, role.name());
        List<UserReviewItemResponse> items = reviews.stream()
                .map(r -> new UserReviewItemResponse(
                        new UserProfileSummary(
                                r.getReviewer().getId(),
                                r.getReviewer().getName(),
                                s3Service.getPublicUrlFromKey(r.getReviewer().getPhotoKey())
                        ),
                        r.getComment(),
                        r.getRating()
                ))
                .toList();
        return new UserReviewsResponse(items);
    }
}
