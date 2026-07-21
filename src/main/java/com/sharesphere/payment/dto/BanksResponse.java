package com.sharesphere.payment.dto;

import java.util.List;

public record BanksResponse(
        List<BankResponse> banks
) {
}
