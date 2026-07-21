package com.sharesphere.payment.dto;

import java.math.BigDecimal;

public record CreateTopUpRequest(
        BigDecimal amount,
        Long bankId
) {}
