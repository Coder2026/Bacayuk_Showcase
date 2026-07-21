package com.sharesphere.borrowtransaction.application;

import com.sharesphere.borrowtransaction.domain.Dispute;
import com.sharesphere.borrowtransaction.domain.DisputeRepository;
import com.sharesphere.borrowtransaction.dto.DisputeSummary;
import com.sharesphere.borrowtransaction.dto.DisputesResponse;
import com.sharesphere.borrowtransaction.dto.GetDisputesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDisputesUseCase {


    private final DisputeRepository disputeRepository;

    public DisputesResponse execute(GetDisputesRequest request) {
        Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<Dispute> page = disputeRepository.findByStatusAndKeyword(request.status(), request.keyword(), pageable);

        List<DisputeSummary> content = page.getContent().stream()
                .map(d -> new DisputeSummary(
                        d.getId(),
                        d.getBorrowTransaction().getId(),
                        d.getBorrowTransaction().getPost().getTitle(),
                        d.getBorrowTransaction().getBorrower().getName(),
                        d.getBorrowTransaction().getLender().getName(),
                        d.getType(),
                        d.getStatus(),
                        d.getCreatedAt()
                ))
                .toList();


        return new DisputesResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
