package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@ToString
@EqualsAndHashCode
public class AuthorizationChallengeTO {
    private final String id;
    private final String type;
    private final String token;
    private final ChallengeStatusEnum status;
    private final Date validated;

    public AuthorizationChallengeTO(
            final String id,
            final String type,
            final String token,
            final ChallengeStatusEnum status,
            final Date validated
    ) {
        this.id = id;
        this.type = type;
        this.token = token;
        this.status = status;
        this.validated = validated;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public ChallengeStatusEnum getStatus() {
        return status;
    }

    public Date getValidated() {
        return validated;
    }
}
