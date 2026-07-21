package com.sharesphere.usermanagement.adapter;

import com.sharesphere.usermanagement.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TokenJpaRepository extends JpaRepository<Token,String> {
    Optional<Token> findByToken(String token);
}
