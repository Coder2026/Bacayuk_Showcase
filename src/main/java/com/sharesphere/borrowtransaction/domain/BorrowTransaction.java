package com.sharesphere.borrowtransaction.domain;

import com.sharesphere.post.domain.Post;
import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTransaction {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;
    @ManyToOne
    @JoinColumn(name = "lender_id")
    private User lender;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "borrowtransaction_proof_handed_over_keys", joinColumns = @JoinColumn(name = "borrowtransaction_id"))
    @Column(name = "key")
    @Builder.Default
    private List<String> proofHandedOverKeys = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "borrowtransaction_proof_returned_keys", joinColumns = @JoinColumn(name = "borrowtransaction_id"))
    @Column(name = "key")
    @Builder.Default
    private List<String> proofReturnedKeys = new ArrayList<>();
    private Instant startDate;
    private Instant endDate;
    private Instant returnDate;
    @Column(columnDefinition = "geography(Point,4326)")
    private Point meetingPoint;

    private String handoverNotes;

    private String returnNotes;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private BorrowStatus status;

    @Column(nullable = false, updatable = false)
    Instant createdAt;
}
