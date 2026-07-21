package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.BorrowStatus;
import com.sharesphere.borrowtransaction.domain.BorrowStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BorrowStatusRepositoryImpl implements BorrowStatusRepository {

    private final BorrowStatusJpaRepository borrowStatusJpaRepository;
    @Override
    public BorrowStatus getReferenceById(Long id) {
        return borrowStatusJpaRepository.getReferenceById(id);
    }
}
