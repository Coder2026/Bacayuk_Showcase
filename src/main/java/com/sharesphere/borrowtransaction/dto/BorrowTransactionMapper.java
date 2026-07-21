package com.sharesphere.borrowtransaction.dto;

import com.sharesphere.borrowtransaction.domain.*;
import com.sharesphere.post.domain.Post;
import com.sharesphere.share.service.S3Service;
import com.sharesphere.usermanagement.domain.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BorrowTransactionMapper {

    @Autowired
    protected S3Service s3Service;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BorrowFinancialTransactionRepository borrowFinancialTransactionRepository;





    @Mapping(target = "borrowTransactionId", source = "t.id")
    @Mapping(target = "startDate", source = "t.startDate")
    @Mapping(target = "endDate", source = "t.endDate")
    @Mapping(target = "returnedDate", source = "t.returnDate")
    @Mapping(target = "borrower", source = "t.borrower", qualifiedByName = "mapUserSummary")
    @Mapping(target = "lender", source = "t.lender", qualifiedByName = "mapUserSummary")
    @Mapping(target = "post", source = "t.post", qualifiedByName = "mapPostSummary")
    @Mapping(target = "meetingPointLatitude", expression = "java(t.getMeetingPoint() != null ? t.getMeetingPoint().getY() : null)")
    @Mapping(target = "meetingPointLongitude", expression = "java(t.getMeetingPoint() != null ? t.getMeetingPoint().getX() : null)")
    @Mapping(target = "status", source = "t.status", qualifiedByName = "mapStatusDetail")
    @Mapping(target = "proofHandedOverKeys", ignore = true)
    @Mapping(target = "proofReturnedKeys", ignore = true)
    @Mapping(target = "handoverNotes", source = "t.handoverNotes")
    @Mapping(target = "returnNotes", source = "t.returnNotes")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "dailyRent", ignore = true)
    @Mapping(target = "guarantee", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "reviewId", ignore = true)
    public abstract BorrowDetailResponse toDetailResponse(BorrowTransaction t);

    public BorrowDetailResponse toDetailResponse(BorrowTransaction t, String currentUserId) {
        BorrowDetailResponse base = toDetailResponse(t);
        TransactionRole role = determineRole(t, currentUserId);

        List<String> proofHandedOverUrls = null;
        if (t.getProofHandedOverKeys() != null) {
            proofHandedOverUrls = t.getProofHandedOverKeys().stream()
                    .map(k -> k == null ? null : s3Service.getPublicUrlFromKey(k))
                    .collect(Collectors.toList());
        }

        List<String> proofReturnedUrls = null;
        if (t.getProofReturnedKeys() != null) {
            proofReturnedUrls = t.getProofReturnedKeys().stream()
                    .map(k -> k == null ? null : s3Service.getPublicUrlFromKey(k))
                    .collect(Collectors.toList());
        }

        var userReview = reviewRepository
                .findByReviewerAndTransaction(currentUserId,t.getId())
                .orElse(null);

        BorrowFinancialTransaction tf =
                borrowFinancialTransactionRepository.findByBorrowTransaction(t)
                        .orElse(null);

        String reviewId = (userReview != null) ? userReview.getId() : null;

        return new BorrowDetailResponse(
                base.borrowTransactionId(),
                base.startDate(),
                base.endDate(),
                base.returnedDate(),
                base.borrower(),
                base.lender(),
                base.post(),
                proofHandedOverUrls,
                proofReturnedUrls,
                base.handoverNotes(),
                base.returnNotes(),
                base.meetingPointLatitude(),
                base.meetingPointLongitude(),
                base.status(),
                role,
                (tf != null ? tf.getDailyRent() : null),
                (tf != null ? tf.getGuarantee() : null),
                (tf != null ? tf.getTotalAmount() : null),
                reviewId
        );
    }


    protected TransactionRole determineRole(BorrowTransaction t, String currentUserId) {
        if (t.getLender() != null && t.getLender().getId().equals(currentUserId)) {
            return TransactionRole.LENDER;
        } else if (t.getBorrower() != null && t.getBorrower().getId().equals(currentUserId)) {
            return TransactionRole.BORROWER;
        }
        return null;
    }

    @Named("mapUserSummary")
    protected UserSummary mapUserSummary(User user) {
        if (user == null) return null;
        return new UserSummary(user.getId(), user.getName());
    }

    @Named("mapPostSummary")
    protected PostSummary mapPostSummary(Post post) {
        if (post == null) return null;
        return new PostSummary(
                post.getId(),
                post.getTitle(),
                s3Service.getPublicUrlFromKey(post.getPhotoKey())
        );
    }

    @Named("mapStatusDetail")
    protected BorrowStatusDetail mapStatusDetail(BorrowStatus status) {
        if (status == null) return null;
        return new BorrowStatusDetail(
                status.getId(),
                status.getName(),
                status.getBorrowerMessage(),
                status.getLenderMessage()
        );
    }
}
