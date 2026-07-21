package com.sharesphere.usermanagement.dto;
import jakarta.validation.constraints.*;

public record LocationRequest(
        @NotNull(message = "Longitude tidak boleh null")
        @Min(value = -180, message = "Longitude minimal -180")
        @Max(value = 180, message = "Longitude maksimal 180")
        Double longitude,

        @NotNull(message = "Latitude tidak boleh null")
        @Min(value = -90, message = "Latitude minimal -90")
        @Max(value = 90, message = "Latitude maksimal 90")
        Double latitude
) {
}