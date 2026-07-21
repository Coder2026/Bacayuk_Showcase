package com.sharesphere.usermanagement.domain;

import java.util.Optional;

public interface TokenRepository {
    Optional<Token> findByToken(String token);
    Token save(Token token);
}
