package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class AuthorizationChallengeRTO {
    private final String type;
    private final String url;
    private final String token;

    public AuthorizationChallengeRTO(final String type, final String url, final String token) {
        this.type = type;
        this.url = url;
        this.token = token;
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
