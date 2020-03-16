package com.dajudge.acme.server.web.exception;

public class BadNonceException extends AcmeException {
    public BadNonceException(final String badNonce) {
        super("Bad nonce: " + badNonce, "badNonce", 400);
    }
}
