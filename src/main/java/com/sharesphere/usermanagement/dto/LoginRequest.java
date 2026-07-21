package com.sharesphere.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginRequest(

    @NotBlank(message = "Email tidak boleh kosong")
    @Size(max = 225, message = "Email maksimal 225 karakter")
    String email,

    
    @NotBlank(message = "Password tidak boleh kosong")
    @Size(max = 50, message = "Password maksimal 50 karakter")
    String password
    ){}
