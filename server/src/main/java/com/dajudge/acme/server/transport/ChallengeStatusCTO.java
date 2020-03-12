package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ChallengeStatusCTO {
    private final AuthorizationChallengeTO challenge;
    private final String authId;

    public ChallengeStatusCTO(
            final AuthorizationChallengeTO challenge,
            final String authId
    ) {
        this.challenge = challenge;
        this.authId = authId;
    }

    public AuthorizationChallengeTO getChallenge() {
        return challenge;
    }

    public String getAuthorizationId() {
        return authId;
    }
}
