package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class GetAuthorizationResponseRTO {
    private final OrderRequestIdentifierRTO identifier;
    private final String status;
    private final List<AuthorizationChallengeRTO> challenges;

    public GetAuthorizationResponseRTO(
            final OrderRequestIdentifierRTO identifier,
            final String status,
            final List<AuthorizationChallengeRTO> challenges
    ) {
        this.identifier = identifier;
        this.status = status;
        this.challenges = challenges;
    }

    public OrderRequestIdentifierRTO getIdentifier() {
        return identifier;
    }

    public String getStatus() {
        return status;
    }

    public List<AuthorizationChallengeRTO> getChallenges() {
        return challenges;
    }
}
