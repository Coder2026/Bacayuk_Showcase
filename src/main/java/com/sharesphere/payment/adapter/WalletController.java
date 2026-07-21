package com.sharesphere.payment.adapter;

import com.sharesphere.payment.application.GetWalletBalanceUseCase;
import com.sharesphere.payment.dto.WalletBalance;
import com.sharesphere.share.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final GetWalletBalanceUseCase getWalletBalanceUseCase;


    @GetMapping("/balance")
    public ResponseEntity<Response<WalletBalance>> getWalletBalance(
            @AuthenticationPrincipal UserDetails currentUser
    ) {
         WalletBalance response = getWalletBalanceUseCase.execute(currentUser.getUsername());
        return ResponseEntity.ok(new Response<>("Saldo wallet berhasil diambil", response));
    }
}