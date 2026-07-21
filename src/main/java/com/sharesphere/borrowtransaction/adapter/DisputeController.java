package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.application.ApproveDisputeUseCase;
import com.sharesphere.borrowtransaction.application.GetDisputeUseCase;
import com.sharesphere.borrowtransaction.application.GetDisputesUseCase;
import com.sharesphere.borrowtransaction.application.RequestDisputeUseCase;
import com.sharesphere.borrowtransaction.application.RejectDisputeUseCase;
import com.sharesphere.borrowtransaction.domain.DisputeStatus;
import com.sharesphere.borrowtransaction.dto.ApproveDisputeRequest;
import com.sharesphere.borrowtransaction.dto.DisputeRequest;
import com.sharesphere.borrowtransaction.dto.DisputeResponse;
import com.sharesphere.borrowtransaction.dto.DisputesResponse;
import com.sharesphere.borrowtransaction.dto.GetDisputesRequest;
import com.sharesphere.share.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disputes")
public class DisputeController {

    private final RequestDisputeUseCase requestDisputeUseCase;
    private final GetDisputeUseCase getDisputeUseCase;
    private final GetDisputesUseCase getDisputesUseCase;
    private final ApproveDisputeUseCase approveDisputeUseCase;
    private final RejectDisputeUseCase rejectDisputeUseCase;


    @PostMapping("/{borrowTransactionId}")
    public ResponseEntity<Response<Void>> createDispute(
            @PathVariable String borrowTransactionId,
            @Valid @RequestBody DisputeRequest request
    ) {
        requestDisputeUseCase.execute(borrowTransactionId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>("Dispute berhasil dibuat", null));
    }


    @GetMapping("/{disputeId}")
    public ResponseEntity<Response<DisputeResponse>> getDispute(@PathVariable String disputeId) {
        DisputeResponse response = getDisputeUseCase.execute(disputeId);
        return ResponseEntity.ok(new Response<>("Detail dispute ditemukan", response));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Response<DisputesResponse>> listDisputes(
            @RequestParam(required = false) DisputeStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        GetDisputesRequest request = new GetDisputesRequest(status, keyword, page, size);
        DisputesResponse response = getDisputesUseCase.execute(request);
        return ResponseEntity.ok(new Response<>("Daftar dispute ditemukan", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{disputeId}/approve")
    public ResponseEntity<Response<Void>> approveDispute(
            @PathVariable String disputeId,
            @Valid @RequestBody ApproveDisputeRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        approveDisputeUseCase.execute(currentUser.getUsername(), disputeId, request);
        return ResponseEntity.ok(new Response<>("Dispute disetujui", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{disputeId}/reject")
    public ResponseEntity<Response<Void>> rejectDispute(@PathVariable String disputeId,@AuthenticationPrincipal UserDetails currentUser) {
        rejectDisputeUseCase.execute(currentUser.getUsername(), disputeId);
        return ResponseEntity.ok(new Response<>("Dispute ditolak", null));
    }

}
