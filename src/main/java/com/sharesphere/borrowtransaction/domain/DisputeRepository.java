package com.sharesphere.borrowtransaction.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface DisputeRepository {
    Dispute save(Dispute dispute);
    Optional<Dispute> findById(String id);
    Page<Dispute> findByStatusAndKeyword(DisputeStatus status,String keyword,
            Pageable pageable
    );
}
