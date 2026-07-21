package com.sharesphere.payment.adapter;


import com.sharesphere.payment.application.*;
import com.sharesphere.payment.dto.*;
import com.sharesphere.payment.domain.ApproveWithdrawalRequest;
import com.sharesphere.share.dto.Response;
import com.sharesphere.payment.domain.WalletTransactionStatus;
import com.sharesphere.payment.domain.WalletTransactionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet-transaction")
public class WalletTransactionController {
    private final CreateTopUpUseCase createTopUpUseCase;
    private final ConfirmTopUpUseCase confirmTopUpUseCase;
    private final CreateWithdrawalUseCase createWithdrawalUseCase;
    private final CancelTopUpUseCase cancelTopUpUseCase;
    private final GetActiveTopUpUseCase getActiveTopUpUseCase;
    private final RejectWithdrawalTransactionUseCase rejectWithdrawalTransactionUseCase;
    private final RejectTopUpTransactionUseCase rejectTopUpTransactionUseCase;
    private final ApproveWithdrawalTransactionUseCase approveWithdrawalTransactionUseCase;
    private final ApproveTopUpUseCase approveTopUpTransactionUseCase;
    private final GetWalletTransactionUseCase getWalletTransactionUseCase;
    private final GetWalletTransactionsUseCase getWalletTransactionsUseCase;
    private final GetActiveWithdrawalUseCase getActiveWithdrawalUseCase;


    @PostMapping("/top-up")
    public ResponseEntity<Response<TopUpResponse>> createTopUp(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody @Valid CreateTopUpRequest request) {

        TopUpResponse response = createTopUpUseCase.execute(currentUser.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>("Top up berhasil dibuat", response));
    }


    @PostMapping("/top-up/{transactionId}/confirm")
    public ResponseEntity<Response<String>> confirmTopUp(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String transactionId,
            @RequestBody @Valid ConfirmTopUpRequest request) {

        confirmTopUpUseCase.execute(currentUser.getUsername(), transactionId, request);

        return ResponseEntity.ok(new Response<>("Top up berhasil dikonfirmasi", null));
    }


    @PostMapping("/withdrawals")
    public ResponseEntity<Response<String>> createWithdrawal(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody @Valid CreateWithdrawalRequest request) {

        createWithdrawalUseCase.execute(currentUser.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>("Withdrawal berhasil dibuat", null));
    }

    @PatchMapping("/top-up/{transactionId}/cancel")
    public ResponseEntity<Response<String>> cancelTopUp(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String transactionId) {

        cancelTopUpUseCase.execute(currentUser.getUsername(), transactionId);

        return ResponseEntity.ok(new Response<>("Top up berhasil dibatalkan", null));
    }

    @GetMapping("/top-up/active")
    public ResponseEntity<Response<TopUpResponse>> getActiveTopUp(
            @AuthenticationPrincipal UserDetails currentUser) {

        TopUpResponse activeTopUp = getActiveTopUpUseCase.execute(currentUser.getUsername());

        if (activeTopUp == null) {
            return ResponseEntity.ok(
                    new Response<>("Tidak ada transaksi top up aktif", null)
            );
        }
        return ResponseEntity.ok(new Response<>("Transaksi top up aktif ditemukan", activeTopUp));
    }

    @GetMapping("/withdrawal/active")
    public ResponseEntity<Response<WithdrawalResponse>> getActiveWithdrawal(
            @AuthenticationPrincipal UserDetails currentUser) {

        var activeWithdrawal = getActiveWithdrawalUseCase.execute(currentUser.getUsername());

        if (activeWithdrawal == null) {
            return ResponseEntity.ok(
                    new Response<>("Tidak ada transaksi withdrawal aktif", null)
            );
        }

        return ResponseEntity.ok(
                new Response<>("Transaksi withdrawal aktif ditemukan", activeWithdrawal)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/top-up/{transactionId}/approve")
    public ResponseEntity<Response<Void>> approveTopUp(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String transactionId) {

        // ApproveTopUpUseCase expects (adminId, walletTransactionId)
        approveTopUpTransactionUseCase.execute(currentUser.getUsername(), transactionId);
        return ResponseEntity.ok(new Response<>("Top up berhasil disetujui", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/top-up/{transactionId}/reject")
    public ResponseEntity<Response<Void>> rejectTopUp(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String transactionId) {

        // RejectTopUpTransactionUseCase expects (adminId, walletTransactionId)
        rejectTopUpTransactionUseCase.execute(currentUser.getUsername(), transactionId);
        return ResponseEntity.ok(new Response<>("Top up berhasil ditolak", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/withdrawals/{transactionId}/approve")
    public ResponseEntity<Response<Void>> approveWithdrawal(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String transactionId,
            @Valid @RequestBody ApproveWithdrawalRequest request) {

        // ApproveWithdrawalTransactionUseCase expects (adminId, withdrawalTransactionId, request)
        approveWithdrawalTransactionUseCase.execute(currentUser.getUsername(), transactionId, request);
        return ResponseEntity.ok(new Response<>("Withdrawal berhasil disetujui", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/withdrawals/{transactionId}/reject")
    public ResponseEntity<Response<Void>> rejectWithdrawal(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable String transactionId) {

        // RejectWithdrawalTransactionUseCase expects (adminId, withdrawalTransactionId)
        rejectWithdrawalTransactionUseCase.execute(currentUser.getUsername(), transactionId);
        return ResponseEntity.ok(new Response<>("Withdrawal berhasil ditolak", null));
    }

    // === GET TRANSACTIONS ===
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{transactionId}")
    public ResponseEntity<Response<Object>> getWalletTransaction(
            @PathVariable String transactionId) {

        Object tx = getWalletTransactionUseCase.execute(transactionId);
        return ResponseEntity.ok(new Response<>("Detail transaksi ditemukan", tx));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Response<WalletTransactionsResponse>> getWalletTransactions(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam(required = false) WalletTransactionStatus status,
            @RequestParam(required = false) WalletTransactionType type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        WalletTransactionRequest req = new WalletTransactionRequest(status, type, keyword, page, size);

        WalletTransactionsResponse transactions = getWalletTransactionsUseCase.execute(req);
        return ResponseEntity.ok(new Response<>("Daftar transaksi ditemukan", transactions));
    }


}
