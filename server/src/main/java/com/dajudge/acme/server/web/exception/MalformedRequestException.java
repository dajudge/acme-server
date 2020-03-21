package com.dajudge.acme.server.web.exception;

public class MalformedRequestException extends AcmeException {

    private MalformedRequestException(final String message) {
        super(message, "malformed", 400);
    }

    public static MalformedRequestException noJsonWebKeyOrKeyIdentifier() {
        return new MalformedRequestException("No jwk or kid provided");
    }
}
