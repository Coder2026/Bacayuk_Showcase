package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.application.CalculateUserRatingUseCase;
import com.sharesphere.borrowtransaction.application.CreateReviewUseCase;
import com.sharesphere.borrowtransaction.application.GetReviewUseCase;
import com.sharesphere.borrowtransaction.application.GetUserReviewUseCase;
import com.sharesphere.borrowtransaction.domain.Review;
import com.sharesphere.borrowtransaction.domain.TransactionRole;
import com.sharesphere.borrowtransaction.dto.ReviewRequest;
import com.sharesphere.borrowtransaction.dto.ReviewResponse;
import com.sharesphere.borrowtransaction.dto.UserReviewsResponse;
import com.sharesphere.borrowtransaction.dto.UserRatingResult;
import com.sharesphere.share.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController{

    private final GetUserReviewUseCase getUserReviewUseCase;
    private final CreateReviewUseCase createReviewUseCase;
    private final CalculateUserRatingUseCase calculateUserRatingUseCase;
    private final GetReviewUseCase getReviewUseCase;


    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserReviewsResponse>> getUserReview(
            @PathVariable String userId,
            @RequestParam TransactionRole role
    ) {
        UserReviewsResponse response = getUserReviewUseCase.execute(userId, role);
        return ResponseEntity.ok(new Response<>("Review berhasil diambil", response));
    }


    @PostMapping("/{borrowTransactionId}")
    public ResponseEntity<Response<String>> createReview(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String borrowTransactionId,
            @Valid @RequestBody ReviewRequest request
    ) {
        createReviewUseCase.execute(currentUser.getUsername(), borrowTransactionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>("Review berhasil ditambahkan", null));
    }

    @GetMapping("/{userId}/rating")
    public ResponseEntity<Response<UserRatingResult>> getUserRating(
            @PathVariable String userId
    ) {
        UserRatingResult ratingResult = calculateUserRatingUseCase.execute(userId);
        return ResponseEntity.ok(new Response<>("Rating user berhasil diambil", ratingResult));
    }

    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<Response<ReviewResponse>> getReview(
            @PathVariable String reviewId
    ){
        ReviewResponse review = getReviewUseCase.execute(reviewId);
        return ResponseEntity.ok(new Response<>("Review berhasil diambil", review));
    }
}
