package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.domain.Dispute;
import com.sharesphere.borrowtransaction.domain.DisputeRepository;
import com.sharesphere.borrowtransaction.domain.DisputeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DisputeRepositoryImpl implements DisputeRepository {

    private  final DisputeJpaRepository disputeJpaRepository;
    @Override
    public Dispute save(Dispute dispute) {
        return disputeJpaRepository.save(dispute);
    }


    @Override
    public Optional<Dispute> findById(String id) {
        return disputeJpaRepository.findById(id);
    }

    @Override
    public Page<Dispute> findByStatusAndKeyword(DisputeStatus status, String keyword, Pageable pageable) {
        return disputeJpaRepository.findByStatusAndKeyword(status,keyword,pageable);
    }

}
