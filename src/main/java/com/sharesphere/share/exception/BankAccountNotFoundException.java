package com.sharesphere.share.exception;

import com.google.api.gax.rpc.NotFoundException;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
