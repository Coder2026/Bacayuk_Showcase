package com.sharesphere.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Bank {
    @Id
    private Long Id;
    private String name;
    private String code;
    private String photoKey;
}
