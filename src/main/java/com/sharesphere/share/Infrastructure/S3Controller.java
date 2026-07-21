package com.sharesphere.share.Infrastructure;

import com.sharesphere.share.dto.Response;
import com.sharesphere.share.dto.PresignedResponse;

import com.sharesphere.share.service.S3Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Facade s3Facade;

    @GetMapping("/presigned-url/profile")
    public ResponseEntity<Response<PresignedResponse>> getProfileUploadUrl(@AuthenticationPrincipal UserDetails currentUser) {
        var data = s3Facade.generateProfileUploadUrl(currentUser.getUsername());
        return ResponseEntity.ok(new Response<>("Presigned URL generated successfully", data));
    }



    @GetMapping("/presigned-url/top-up")
    public ResponseEntity<Response<PresignedResponse>> getTopUploadUrl(@AuthenticationPrincipal UserDetails currentUser,@PathVariable String transactionId) {
        var data = s3Facade.generateTopUpProofUploadUrl(currentUser.getUsername(),transactionId);
        return ResponseEntity.ok(new Response<>("Presigned URL generated successfully", data));
    }


    @GetMapping("/presigned-url/post")
    public ResponseEntity<Response<PresignedResponse>> getPostUploadUrl(@AuthenticationPrincipal UserDetails currentUser) {
        var data = s3Facade.generateNewPostUploadUrl(currentUser.getUsername());
        return ResponseEntity.ok(new Response<>("Presigned URL generated successfully", data));
    }



    @GetMapping("/presigned-url/post/update")
    public ResponseEntity<Response<PresignedResponse>> getUpdatePostUploadUrl(
            @RequestParam String postId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        var data = s3Facade.generateUpdatePostUploadUrl(postId, currentUser.getUsername());
        return ResponseEntity.ok(new Response<>("Presigned URL for update generated successfully", data));
    }

    @GetMapping("/presigned-url/transaction/proof")
    public ResponseEntity<Response<PresignedResponse>> getHandoverProofUploadUrl(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam String transactionId
    ) {
        var data = s3Facade.generateTransactionProofUploadUrl(currentUser.getUsername(), transactionId);
        return ResponseEntity.ok(new Response<>("Presigned URL for handover proof generated successfully", data));
    }

    @GetMapping("/presigned-url/dispute")
    public ResponseEntity<Response<PresignedResponse>> getDisputeProofUploadUrl(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam String transactionId
    ) {
        var data = s3Facade.generateDisputeProofUploadUrl(currentUser.getUsername(), transactionId);
        return ResponseEntity.ok(new Response<>("Presigned URL for dispute proof generated successfully", data));
    }

    @GetMapping("/presigned-url/admin/withdrawal/proof")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<PresignedResponse>> getWithdrawalProofUploadUrl(
            @RequestParam String withdrawalId,
            @AuthenticationPrincipal UserDetails admin
    ) {
        var data = s3Facade.generateWithdrawalProofUploadUrl(admin.getUsername(), withdrawalId);
        return ResponseEntity.ok(
                new Response<>("Presigned URL for withdrawal proof generated successfully", data)
        );
    }
}