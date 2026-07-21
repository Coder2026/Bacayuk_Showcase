package com.sharesphere.post.domain;

import com.sharesphere.post.dto.PostRequest;
import com.sharesphere.usermanagement.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "posts")
public class Post {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;
    private String title;
    private String author;
    private String isbn;
    private Integer publishYear;
    private Integer pages;
    private String language;
    @Column(length = 500)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookFormat bookFormat;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PostStatus status = PostStatus.AVAILABLE;

    private BigDecimal guarantee;
    private BigDecimal dailyRentFee;
    private String photoKey;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public void update(PostRequest postRequest,Category category) {
        this.title = postRequest.title();
        this.author = postRequest.author();
        this.isbn = postRequest.isbn();
        this.publishYear = postRequest.publishYear();
        this.pages = postRequest.pages();
        this.language = postRequest.language();
        this.description = postRequest.description();
        this.category = category;
        this.guarantee = postRequest.guarantee();
        this.dailyRentFee = postRequest.dailyRentFee();
        this.photoKey = postRequest.photoKey();
        this.bookFormat = postRequest.bookFormat();
    }
}