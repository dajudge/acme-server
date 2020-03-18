/*
 * Copyright 2020 Alex Stockinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.dajudge.acme.server.web.exception;

import lombok.ToString;

import javax.ws.rs.core.Response;

public abstract class AcmeException extends RuntimeException {
    private final Response response;

    private AcmeException(final String message, final Throwable cause, final Response response) {
        super(message, cause);
        this.response = response;
    }

    private AcmeException(final String message, final Response response) {
        super(message);
        this.response = response;
    }

    AcmeException(final String message, final String type, final int status) {
        this(message, response(type, message, status));
    }

    AcmeException(final String message, final String type, final int status, final Throwable cause) {
        this(message, cause, response(type, message, status));
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

    public Response getResponse() {
        return response;
    }

    @ToString
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

}


