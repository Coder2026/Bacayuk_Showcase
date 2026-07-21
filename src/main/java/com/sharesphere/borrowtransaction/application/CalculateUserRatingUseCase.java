package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.adapter.ReviewJpaRepository;
import com.sharesphere.borrowtransaction.domain.ReviewRepository;
import com.sharesphere.borrowtransaction.dto.UserRatingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CalculateUserRatingUseCase {


    private final ReviewRepository reviewRepository;

    public UserRatingResult execute(String userId) {

        var result = reviewRepository.findAvgRatingByUser(userId);

        double borrowerRating = result.getBorrowerAvg() != null
                ? result.getBorrowerAvg()
                : 0.0;

        double lenderRating = result.getLenderAvg() != null
                ? result.getLenderAvg()
                : 0.0;

        return new UserRatingResult(borrowerRating, lenderRating);
    }
}
