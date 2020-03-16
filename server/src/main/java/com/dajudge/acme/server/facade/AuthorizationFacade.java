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

package com.dajudge.acme.server.facade;

import com.dajudge.acme.server.model.Account;
import com.dajudge.acme.server.model.Order;
import com.dajudge.acme.server.model.AuthorizationRequest;
import com.dajudge.acme.server.repository.CentralRepository;
import com.dajudge.acme.server.transport.*;
import com.dajudge.acme.server.facade.mapper.AuthorizationChallengeMapper;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Collection;

@Dependent
public class AuthorizationFacade {

    private final CentralRepository centralRepository;

    @Inject
    public AuthorizationFacade(final CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    public AuthorizationStatusCTO getAuthorizationById(final String authId) {
        final AuthorizationRequest authorizationRequest = centralRepository.getAccounts().values().stream()
                .map(Account::getOrders)
                .flatMap(Collection::stream)
                .map(Order::getIdentifiers)
                .flatMap(Collection::stream)
                .filter(it -> it.getId().equals(authId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unknown authorization ID: " + authId));
        final AuthorizationRequestTO orderRequest = new AuthorizationRequestTO(
                authorizationRequest.getId(),
                authorizationRequest.getType(),
                authorizationRequest.getValue()
        );
        final AuthorizationTO authorization = new AuthorizationTO(
                authorizationRequest.getId(),
                AuthorizationChallengeMapper.toTransportObjects(authorizationRequest.getChallenges())
        );
        return new AuthorizationStatusCTO(orderRequest, authorization, statusOf(authorization.getChallenges()));
    }

    private AuthorizationStatusEnum statusOf(final Collection<AuthorizationChallengeTO> challenges) {
        return challenges.stream().anyMatch(challenge -> challenge.getStatus() == ChallengeStatusEnum.VALID)
                ? AuthorizationStatusEnum.VALID
                : AuthorizationStatusEnum.PENDING;
    }
}
