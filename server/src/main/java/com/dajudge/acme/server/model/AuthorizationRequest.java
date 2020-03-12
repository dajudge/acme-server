package com.dajudge.acme.server.model;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationRequest {
    private String id;
    private String type;
    private String value;
    private List<AuthorizationChallenge> challenges = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public List<AuthorizationChallenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(final List<AuthorizationChallenge> challenges) {
        this.challenges = challenges;
    }
}
