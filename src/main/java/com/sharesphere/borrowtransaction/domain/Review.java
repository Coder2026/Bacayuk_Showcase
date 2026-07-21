package com.sharesphere.borrowtransaction.domain;

import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "borrow_transaction_id")
    private BorrowTransaction borrowTransaction;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;


    @ManyToOne
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    private String comment;
    private double rating;
}
