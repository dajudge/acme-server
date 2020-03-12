package com.dajudge.acme.server.facade.mapper;

import com.dajudge.acme.server.model.AuthorizationChallenge;
import com.dajudge.acme.server.transport.AuthorizationChallengeTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AuthorizationChallengeMapper {
    public static AuthorizationChallengeTO toTransportObject(final AuthorizationChallenge authorizationChallenge) {
        return new AuthorizationChallengeTO(
                authorizationChallenge.getId(),
                authorizationChallenge.getType(),
                authorizationChallenge.getToken(),
                authorizationChallenge.getStatus(),
                authorizationChallenge.getValidated()
        );
    }

    public static List<AuthorizationChallengeTO> toTransportObjects(
            final List<AuthorizationChallenge> challenges
    ) {
        return challenges.stream().map(AuthorizationChallengeMapper::toTransportObject).collect(toList());
    }
}
