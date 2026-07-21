package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowStatusJpaRepository extends JpaRepository<BorrowStatus, Long> {

}
