package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class CheckChallengeResponseRTO{
    private final String type;
    private final String url;
    private final String token;
    private final String status;
    private final String validated;

    public CheckChallengeResponseRTO(
            final String type,
            final String url,
            final String token,
            final String status,
            final String validated
    ) {
        this.type = type;
        this.url = url;
        this.token = token;
        this.status = status;
        this.validated = validated;
    }

    public String getStatus() {
        return status;
    }

    public String getValidated() {
        return validated;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }
}
