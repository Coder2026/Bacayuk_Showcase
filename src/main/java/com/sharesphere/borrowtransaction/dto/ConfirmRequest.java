package com.sharesphere.borrowtransaction.dto;

import java.util.List;

public record ConfirmRequest(
        List<String> photoKeys,
        String notes
) {
}
