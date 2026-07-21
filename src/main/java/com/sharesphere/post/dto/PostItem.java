package com.sharesphere.post.dto;

import com.sharesphere.post.domain.PostStatus;

import java.math.BigDecimal;

public record PostItem(String id, String photoUrl, String title, BigDecimal dailyRentFee,  PostStatus status) {

}
