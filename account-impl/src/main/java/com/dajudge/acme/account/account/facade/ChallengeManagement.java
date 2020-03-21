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

package com.dajudge.acme.account.account.facade;

import com.dajudge.acme.account.facade.ChallengeFacade;
import com.dajudge.acme.account.facade.transport.ChallengeStatusCTO;
import com.dajudge.acme.account.account.business.ChallengeVerifier;
import com.dajudge.acme.account.account.model.Account;
import com.dajudge.acme.account.account.model.AuthorizationChallenge;
import com.dajudge.acme.account.account.model.AuthorizationRequest;
import com.dajudge.acme.account.account.model.Order;
import com.dajudge.acme.account.account.repository.CentralRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;

import static com.dajudge.acme.account.account.facade.mapper.AuthorizationChallengeMapper.toTransportObject;

@Singleton
public class ChallengeManagement implements ChallengeFacade {
    private final CentralRepository centralRepository;
    private final ChallengeVerifier verifier;

    @Inject
    public ChallengeManagement(
            final CentralRepository centralRepository,
            final ChallengeVerifier verifier
    ) {
        this.centralRepository = centralRepository;
        this.verifier = verifier;
    }

    @Override
    public void verifyChallenges() {
        verifier.verifyChallenges();
    }

    @Override
    public ChallengeStatusCTO getChallengeStatus(final String challengeId) {
        final AuthorizationRequest request = centralRepository.getAccounts().stream()
                .map(Account::getOrders).flatMap(Collection::stream)
                .map(Order::getIdentifiers).flatMap(Collection::stream)
                .filter(hasChallengeWithId(challengeId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such challenge: " + challengeId));
        final AuthorizationChallenge challenge = request.getChallenges().stream()
                .filter(it -> it.getId().equals(challengeId))
                .findAny()
                .get();
        challenge.setValidated(new Date());
        return new ChallengeStatusCTO(toTransportObject(challenge), request.getId());
    }

    private Predicate<? super AuthorizationRequest> hasChallengeWithId(final String challengeId) {
        return it -> it.getChallenges().stream().anyMatch(c -> c.getId().equals(challengeId));
    }
}
