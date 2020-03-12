package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@ToString
@EqualsAndHashCode
public class AuthorizationTO {
    private final Collection<AuthorizationChallengeTO> challenges;
    private final String id;

    public AuthorizationTO(
            final String id,
            final Collection<AuthorizationChallengeTO> challenges
    ) {
        this.id = id;
        this.challenges = challenges;
    }

    public Collection<AuthorizationChallengeTO> getChallenges() {
        return challenges;
    }

    public String getId() {
        return id;
    }
}
