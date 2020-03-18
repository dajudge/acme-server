package com.dajudge.acme.server.web.exception;

public class UnauthorizedException extends AcmeException {
    private UnauthorizedException(final String message) {
        super(message, "unauthorized", 401);
    }

    public static UnauthorizedException noKeyIdProvided() {
        return new UnauthorizedException("No JWS Key ID provided");
    }

    public static UnauthorizedException unknownKeyId(final String kid) {
        return new UnauthorizedException("Unknown Key ID: " + kid);
    }
}
