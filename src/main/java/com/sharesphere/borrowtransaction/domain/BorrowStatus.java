package com.sharesphere.borrowtransaction.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowStatus {
    @Id
    Long id;
    String name;
    String description;
    String borrowerMessage;
    String lenderMessage;
}
