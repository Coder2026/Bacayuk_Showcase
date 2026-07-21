package com.sharesphere.payment.domain;

import java.util.Optional;

public interface WalletRepository {
    Wallet save(Wallet wallet);
   Optional<Wallet> findByOwnerId(String ownerId);
}
