package com.sharesphere.payment.adapter;

import com.sharesphere.payment.application.*;
import com.sharesphere.payment.domain.BankService;
import com.sharesphere.payment.dto.BankAccountRequest;
import com.sharesphere.payment.dto.BankAccountResponse;
import com.sharesphere.payment.dto.BanksResponse;
import com.sharesphere.share.dto.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banks")
public class BankController {
    private final BankService bankService;
    private final CreateBankAccountUseCase createBankAccountUseCase;
    private final UpdateBankAccountUseCase updateBankAccountUseCase;
    private final GetBankAccountUseCase getBankAccountUseCase;

    @GetMapping
    public ResponseEntity<Response<BanksResponse>> getAllBanks() {
        BanksResponse banksResponse = bankService.getAllBanks();
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Bank berhasil diambil", banksResponse));
    }

    @PostMapping("/bank-accounts")
    public ResponseEntity<Response<BankAccountResponse>> createBankAccount(@RequestBody @Valid BankAccountRequest bankAccountRequest, @AuthenticationPrincipal UserDetails currentUser) {
        BankAccountResponse bankAccountResponse = createBankAccountUseCase.execute(currentUser.getUsername(), bankAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>("Rekening bank berhasil dibuat", bankAccountResponse));
    }


    @GetMapping("/bank-accounts")
    public ResponseEntity<Response<BankAccountResponse>> getMyBankAccount(
            @AuthenticationPrincipal UserDetails currentUser) {

        BankAccountResponse bankAccountResponse = getBankAccountUseCase.execute(currentUser.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>("Rekening bank berhasil diambil", bankAccountResponse));
    }

    @PutMapping("/bank-accounts/{bankAccountId}")
    public ResponseEntity<Response<BankAccountResponse>> updateBankAccount(@PathVariable @NotBlank String bankAccountId, @RequestBody @Valid BankAccountRequest bankAccountRequest, @AuthenticationPrincipal UserDetails currentUser) {

        BankAccountResponse bankAccountResponse = updateBankAccountUseCase.execute(currentUser.getUsername(),bankAccountId,bankAccountRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Rekening bank berhasil diperbarui", bankAccountResponse));
    }

}
