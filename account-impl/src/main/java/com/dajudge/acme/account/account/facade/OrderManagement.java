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

import com.dajudge.acme.account.facade.OrderFacade;
import com.dajudge.acme.account.facade.transport.AuthorizationRequestTO;
import com.dajudge.acme.account.facade.transport.ChallengeStatusEnum;
import com.dajudge.acme.account.facade.transport.OrderStatusEnum;
import com.dajudge.acme.account.facade.transport.OrderTO;
import com.dajudge.acme.account.account.business.CertificationAuthorityRegistry;
import com.dajudge.acme.account.account.facade.mapper.OrderMapper;
import com.dajudge.acme.account.account.model.Account;
import com.dajudge.acme.account.account.model.AuthorizationChallenge;
import com.dajudge.acme.account.account.model.AuthorizationRequest;
import com.dajudge.acme.account.account.model.Order;
import com.dajudge.acme.account.account.repository.CentralRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.dajudge.acme.account.facade.transport.OrderStatusEnum.PENDING;
import static java.util.stream.Collectors.toList;

@Singleton
public class OrderManagement implements OrderFacade {
    private final CentralRepository centralRepository;
    private final CertificationAuthorityRegistry certificationAuthorityRegistry;

    @Inject
    public OrderManagement(
            final CentralRepository centralRepository,
            final CertificationAuthorityRegistry certificationAuthorityRegistry
    ) {
        this.centralRepository = centralRepository;
        this.certificationAuthorityRegistry = certificationAuthorityRegistry;
    }

    @Override
    public OrderTO createOrder(final String accountId, final List<AuthorizationRequestTO> orderIdentifiers) {
        final Order order = createOrderWithRequests(orderIdentifiers);
        centralRepository.getAccounts()
                .stream()
                .filter(it -> it.getId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unknown account: " + accountId)) // TODO proper error
                .getOrders()
                .add(order);
        return OrderMapper.toTransportObject(order);
    }

    @Override
    public OrderTO finalizeOrder(final String orderId, final byte[] csr) {
        final Order order = centralRepository.getAccounts().stream()
                .map(Account::getOrders).flatMap(Collection::stream)
                .filter(it -> orderId.equals(it.getId()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unknown order: " + orderId));
        order.setStatus(OrderStatusEnum.VALID);
        order.setCertificateChain(certificationAuthorityRegistry.createCertificate(csr));
        return OrderMapper.toTransportObject(order);
    }

    private Order createOrderWithRequests(final List<AuthorizationRequestTO> orderIdentifiers) {
        final Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setExpires(new Date(System.currentTimeMillis() + 10 * 60 * 1000));
        order.setStatus(PENDING);
        order.setIdentifiers(orderIdentifiers.stream()
                .map(this::createRequestWithChallenge)
                .collect(toList()));
        return order;
    }

    private AuthorizationRequest createRequestWithChallenge(final AuthorizationRequestTO to) {
        final AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setId(UUID.randomUUID().toString());
        authorizationRequest.setType(to.getType());
        authorizationRequest.setValue(to.getValue());
        final AuthorizationChallenge challenge = new AuthorizationChallenge();
        challenge.setId(UUID.randomUUID().toString());
        challenge.setStatus(ChallengeStatusEnum.PENDING);
        challenge.setToken(UUID.randomUUID().toString());
        challenge.setType("http-01");
        authorizationRequest.getChallenges().add(challenge);
        return authorizationRequest;
    }

    @Override
    public OrderTO getOrder(final String accountId, final String orderId) {
        return listOrders(accountId)
                .stream()
                .filter(it -> it.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown order: " + orderId)); // TODO proper error handling
    }

    @Override
    public List<OrderTO> listOrders(final String accountId) {
        final List<Order> orders = centralRepository.getAccounts()
                .stream()
                .filter(it -> accountId.equals(it.getId()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unknown account: " + accountId)) // TODO proper error handling
                .getOrders();
        return OrderMapper.toTransportObjects(orders);
    }
}
