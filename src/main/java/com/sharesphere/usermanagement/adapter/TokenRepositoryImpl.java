package com.sharesphere.usermanagement.adapter;

import com.sharesphere.usermanagement.domain.Token;
import com.sharesphere.usermanagement.domain.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@AllArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private  final TokenJpaRepository tokenJpaRepository;

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenJpaRepository.findByToken(token);
    }

    @Override
    public Token save(Token token) {
        return tokenJpaRepository.save(token);
    }
}
