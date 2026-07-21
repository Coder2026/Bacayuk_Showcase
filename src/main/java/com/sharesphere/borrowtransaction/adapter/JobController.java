package com.sharesphere.borrowtransaction.adapter;

import com.sharesphere.borrowtransaction.application.AutoCancelExpiredBorrowUseCase;
import com.sharesphere.borrowtransaction.application.AutoCompleteReturnedBorrowUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cron/jobs")
@Slf4j
public class JobController {

    private final AutoCancelExpiredBorrowUseCase autoCancelExpiredBorrowUseCase;
    private final AutoCompleteReturnedBorrowUseCase autoCompleteReturnedBorrowUseCase;

    private boolean isAuthorized(String secret) {
        String expected = System.getenv("CRON_SECRET_KEY");
        return expected != null && expected.equals(secret);
    }

    // 🔹 Job untuk auto-cancel (gagal deal / tidak ketemu)
    @PostMapping("/auto-cancel")
    public ResponseEntity<String> autoCancel(@RequestHeader("X-CRON-SECRET") String secret) {

        if (!isAuthorized(secret)) {
            log.warn("Unauthorized access attempt to /auto-cancel");
            return ResponseEntity.status(403).body("Forbidden");
        }


        int total = autoCancelExpiredBorrowUseCase.execute();
        return ResponseEntity.ok("Auto-cancel selesai. Total transaksi dibatalkan: " + total);
    }

    // 🔹 Job untuk auto-complete (barang dikembalikan & verifikasi)
    @PostMapping("/auto-complete")
    public ResponseEntity<String> autoComplete(@RequestHeader("X-CRON-SECRET") String secret) {

        if (!isAuthorized(secret)) {
            log.warn("Unauthorized access attempt to /auto-complete");
            return ResponseEntity.status(403).body("Forbidden");
        }

        int total = autoCompleteReturnedBorrowUseCase.execute();
        return ResponseEntity.ok("Auto-complete selesai. Total transaksi diselesaikan: " + total);
    }
}