package com.sharesphere.payment.domain;

import jakarta.validation.constraints.NotBlank;

public record ApproveWithdrawalRequest(
        @NotBlank String proofKey
) {
}
