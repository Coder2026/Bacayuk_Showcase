package com.sharesphere.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

        @NotBlank
        String token,
        @NotBlank(message = "Password tidak boleh kosong")
        @Size(max = 50, message = "Password maksimal 50 karakter")
        String newPassword
) {
}
