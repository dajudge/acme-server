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

package com.dajudge.acme.account.account.business;

import com.dajudge.acme.challenge.ChallengeType;
import com.dajudge.acme.common.Clock;
import com.dajudge.acme.account.account.model.Account;
import com.dajudge.acme.account.account.model.AuthorizationChallenge;
import com.dajudge.acme.account.account.model.AuthorizationRequest;
import com.dajudge.acme.account.account.model.Order;
import com.dajudge.acme.account.account.repository.CentralRepository;
import com.dajudge.acme.account.facade.transport.ChallengeStatusEnum;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static com.dajudge.acme.account.facade.transport.ChallengeStatusEnum.VALID;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static org.jose4j.jwk.JsonWebKey.Factory.newJwk;

@Singleton
public class ChallengeVerifier {
    private static final Logger LOG = LoggerFactory.getLogger(ChallengeVerifier.class);

    private final Clock clock;
    private final CentralRepository centralRepository;
    private final Map<String, ChallengeType> challenges;

    @Inject
    public ChallengeVerifier(
            final Clock clock,
            final CentralRepository centralRepository,
            final @Any Instance<ChallengeType> challenges
    ) {
        this.clock = clock;
        this.centralRepository = centralRepository;
        this.challenges = challenges.stream().collect(toMap(
                it -> it.getIdentifierType() + "/" + it.getChallengeType(),
                it -> it
        ));
        LOG.info("Available challenge types: {}", this.challenges.keySet());
    }

    public void verifyChallenges() {
        centralRepository.getAccounts().forEach(account -> account.getOrders().stream()
                .map(Order::getIdentifiers).flatMap(Collection::stream)
                .forEach(request -> {
                    request.getChallenges().stream()
                            .filter(it -> it.getStatus() == ChallengeStatusEnum.PENDING)
                            .forEach(challenge -> {
                                if (verify(account, request, challenge)) {
                                    challenge.setStatus(VALID);
                                    challenge.setValidated(new Date(clock.now()));
                                }
                            });
                }));
    }

    private boolean verify(
            final Account account,
            final AuthorizationRequest request,
            final AuthorizationChallenge challenge
    ) {
        final ChallengeType challengeType = challenges.get(request.getType() + "/" + challenge.getType());
        if (challengeType == null) {
            throw new RuntimeException(format(
                    "Unknown request / challenge combination: %s/%s",
                    request.getType(),
                    challenge.getType()
            ));
        }
        return challengeType.verifyChallenge(request.getValue(), challenge.getToken(), thumbprint(account));
    }

    private byte[] thumbprint(final Account account) {
        try {
            return newJwk(account.getPublicKey()).calculateThumbprint("SHA256");
        } catch (final JoseException e) {
            throw new RuntimeException(String.format(
                    "Failed to calculate thumbprint for JWK of Account %s",
                    account.getId()
            ));
        }
    }
}
