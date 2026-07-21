package com.sharesphere.borrowtransaction.dto;

import java.math.BigDecimal;

public record ApproveDisputeRequest(
        BigDecimal amountToPay,
        String adminComments
) {
}
