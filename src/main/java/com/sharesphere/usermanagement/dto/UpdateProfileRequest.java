package com.sharesphere.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "Nama tidak boleh kosong")
        @Size(max = 50, message = "Nama maksimal 50 karakter")
        String name,
        String photoKey
) {

    public boolean hasPhotoKey() {
        return photoKey != null && !photoKey.trim().isEmpty();
    }
    @JsonIgnore
    public String getProcessedPhotoKey() {
        return hasPhotoKey() ? photoKey.trim() : null;
    }}
