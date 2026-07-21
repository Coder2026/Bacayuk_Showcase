package com.sharesphere.borrowtransaction.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dispute {

    
    @Id
    String id;
    @OneToOne
    @JoinColumn(name = "borrow_transaction_id")
    BorrowTransaction borrowTransaction;
    String disputeReason;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dispute_lender_evidence_keys", joinColumns = @JoinColumn(name = "dispute_id"))
    @Column(name = "key")
    @Builder.Default
    List<String> lenderEvidenceKeys = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dispute_borrower_evidence_keys", joinColumns = @JoinColumn(name = "dispute_id"))
    @Column(name = "key")
    @Builder.Default
    List<String> borrowerEvidenceKeys = new ArrayList<>();

    String resolutionNote;

    String resolvedBy;
    Instant resolvedAt;
    Instant createdAt;


    @Enumerated(EnumType.STRING)
    DisputeType type;

    @Enumerated(EnumType.STRING)
    DisputeStatus status;


}
