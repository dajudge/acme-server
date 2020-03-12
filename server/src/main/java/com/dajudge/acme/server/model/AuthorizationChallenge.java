package com.dajudge.acme.server.model;

import com.dajudge.acme.server.transport.ChallengeStatusEnum;

import java.util.Date;

public class AuthorizationChallenge {
    private String id;
    private String type;
    private String token;
    private ChallengeStatusEnum status;
    private Date validated;

    public void setId(final String id) {
        this.id = id;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setStatus(final ChallengeStatusEnum status) {
        this.status = status;
    }

    public void setValidated(final Date validated) {
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
