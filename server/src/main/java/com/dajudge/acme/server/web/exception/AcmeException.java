package com.dajudge.acme.server.web.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

abstract class AcmeException extends WebApplicationException {
    public static final class ProblemDocument {
        private final String type;
        private final String detail;
        private final int status;

        ProblemDocument(final String type, final String detail, final int status) {
            this.type = type;
            this.detail = detail;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public String getType() {
            return type;
        }

        public String getDetail() {
            return detail;
        }
    }

    protected AcmeException(final String message, final String type, final int status) {
        super(message, response(type, message, status));
    }

    protected AcmeException(final String message, final String type, final int status, final Throwable cause) {
        super(message, cause, response(type, message, status));
    }

    private static Response response(final String type, final String detail, final int status) {
        return Response.status(status)
                .entity(new ProblemDocument(
                        "urn:ietf:params:acme:error:" + type,
                        detail,
                        status
                ))
                .type("application/problem+json")
                .build();
    }
}
