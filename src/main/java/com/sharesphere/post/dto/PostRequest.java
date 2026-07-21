package com.sharesphere.post.dto;


import com.sharesphere.post.domain.BookFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PostRequest(
    @NotBlank(message = "Judul tidak boleh kosong")
    @Size(max = 255,message = "Judul maksimal 255 karakter")
    String title,
    @NotBlank(message = "Penulis tidak boleh kosong")
    @Size(max = 255, message = "Nama penulis maksimal 255 karakter")
    String author,
    @Size(max = 20, message = "ISBN maksimal 20 karakter")
    String isbn,
    @Positive(message = "Jumlah halaman harus lebih dari 0")
    Integer pages,
    @Size(max = 100, message = "Bahasa maksimal 100 karakter")
    String language,
    @Min(value = 0, message = "Tahun terbit tidak boleh negatif")
    Integer publishYear,
    @Size(max = 500, message = "Description maksimal 500 karakter")
    String description,
    @NotNull(message = "Kategori tidak boleh kosong")
    Long categoryId,
    @NotNull(message = "Jaminan tidak boleh kosong")
    @DecimalMin(value = "0.0", message = "Jaminan tidak boleh kurang dari 0")
    @Digits(integer=13, fraction=2, message="Format jaminan tidak valid, maksimal 13 digit dan 2 desimal")
    BigDecimal guarantee,
    @NotNull(message = "Biaya sewa harian tidak boleh kosong")
    @DecimalMin(value = "1000.0", message = "Biaya sewa harian minimal Rp1000")
    @Digits(integer = 13, fraction = 2, message = "Format biaya sewa tidak valid, maksimal 13 digit dan 2 desimal")
    BigDecimal dailyRentFee,
    @NotNull(message = "Format buku tidak boleh kosong")
    BookFormat bookFormat,
    @NotBlank(message = "Photo key tidak boleh kosong")
    @Size(max = 255, message = "photo key maksimal 255 karakter")
    String photoKey
){}
