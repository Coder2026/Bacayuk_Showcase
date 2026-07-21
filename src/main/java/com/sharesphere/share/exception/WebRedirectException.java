package com.sharesphere.share.exception;

import lombok.Getter;
@Getter
public class WebRedirectException extends RuntimeException {
    private final String path;
    private final String messageParam;

    public WebRedirectException(String path, String messageParam) {
        super("Redirect to path: " + path + " with message: " + messageParam);
        this.path = path;
        this.messageParam = messageParam;
    }

}



