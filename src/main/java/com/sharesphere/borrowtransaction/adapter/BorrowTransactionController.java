package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.application.*;
import com.sharesphere.borrowtransaction.domain.TransactionRole;
import com.sharesphere.borrowtransaction.dto.*;
import com.sharesphere.share.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/borrow-transactions")
public class BorrowTransactionController {

    private final RequestBorrowUseCase requestBorrowUseCase;
    private final AcceptedBorrowUseCase acceptedBorrowUseCase;
    private final RejectBorrowUseCase rejectBorrowUseCase;
    private final CancelBorrowUseCase cancelBorrowUseCase;
    private final ConfirmHandoverUseCase confirmHandoverUseCase;
    private final CompleteBorrowUseCase completeBorrowUseCase;
    private final GetBorrowTransactionUseCase getBorrowTransactionUseCase;
    private final GetBorrowTransactionsUseCase getBorrowTransactionsUseCase;
    private final ReturnedBorrowUseCase returnedBorrowUseCase;

    @PostMapping("/request")
    ResponseEntity<Response<BorrowDetailResponse>> requestBorrow(
            @RequestBody @Valid BorrowRequest borrowRequest,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = requestBorrowUseCase.execute(currentUser.getUsername(), borrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>("Permintaan peminjaman berhasil dibuat", response));
    }

    @GetMapping("/{borrowTransactionId}")
    ResponseEntity<Response<BorrowDetailResponse>> getTransactionDetail(
            @PathVariable String borrowTransactionId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = getBorrowTransactionUseCase.execute(currentUser.getUsername(),borrowTransactionId);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Detail transaksi ditemukan", response));
    }

    @GetMapping
    ResponseEntity<Response<BorrowListResponse>> getTransactions(
            @RequestParam(required = false) TransactionRole role,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowListResponse response = getBorrowTransactionsUseCase.execute(currentUser.getUsername(), role);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Daftar transaksi ditemukan", response));
    }


    @PostMapping("/{borrowTransactionId}/accept")
    ResponseEntity<Response<BorrowDetailResponse>> acceptBorrow(
            @PathVariable String borrowTransactionId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = acceptedBorrowUseCase.execute(currentUser.getUsername(),borrowTransactionId);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Permintaan diterima", response));
    }

    @PostMapping("/{borrowTransactionId}/reject")
    ResponseEntity<Response<BorrowDetailResponse>> rejectBorrow(
            @PathVariable String borrowTransactionId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = rejectBorrowUseCase.execute(currentUser.getUsername(), borrowTransactionId);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Permintaan ditolak", response));
    }

    @PostMapping("/{borrowTransactionId}/cancel")
    ResponseEntity<Response<BorrowDetailResponse>> cancelBorrow(
            @PathVariable String borrowTransactionId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = cancelBorrowUseCase.execute(currentUser.getUsername(), borrowTransactionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>("Permintaan peminjaman dibatalkan", response));
    }

    @PostMapping("/{borrowTransactionId}/handover")
    ResponseEntity<Response<BorrowDetailResponse>> confirmHandover(
            @PathVariable String borrowTransactionId,
            @RequestBody @Valid ConfirmRequest request,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = confirmHandoverUseCase.execute(currentUser.getUsername(),borrowTransactionId, request);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Penyerahan barang dikonfirmasi", response));
    }


    @PostMapping("/{borrowTransactionId}/returned")
    ResponseEntity<Response<BorrowDetailResponse>> markReturned(
            @PathVariable String borrowTransactionId,
            @RequestBody @Valid ConfirmRequest request,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = returnedBorrowUseCase.execute(currentUser.getUsername(), borrowTransactionId, request);
        return ResponseEntity.ok(new Response<>("Borrower menandai buku sudah dikembalikan", response));
    }

    @PostMapping("/{borrowTransactionId}/confirm-return")
    ResponseEntity<Response<BorrowDetailResponse>> confirmReturn(
            @PathVariable String borrowTransactionId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        BorrowDetailResponse response = completeBorrowUseCase.execute(currentUser.getUsername(),borrowTransactionId);
        return ResponseEntity.ok(new Response<>("Pengembalian buku dikonfirmasi", response));
    }
}
