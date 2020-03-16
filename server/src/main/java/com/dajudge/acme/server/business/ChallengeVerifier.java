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

package com.dajudge.acme.server.business;

import com.dajudge.acme.ca.Clock;
import com.dajudge.acme.server.adapter.AppConfigAdapter;
import com.dajudge.acme.server.model.Account;
import com.dajudge.acme.server.model.AuthorizationChallenge;
import com.dajudge.acme.server.model.AuthorizationRequest;
import com.dajudge.acme.server.model.Order;
import com.dajudge.acme.server.repository.CentralRepository;
import com.dajudge.acme.server.transport.ChallengeStatusEnum;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import static com.dajudge.acme.server.transport.ChallengeStatusEnum.VALID;
import static com.dajudge.acme.server.util.StringUtils.base64url;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static org.jose4j.jwk.JsonWebKey.Factory.newJwk;

@Singleton
public class ChallengeVerifier {
    private static final Logger LOG = LoggerFactory.getLogger(ChallengeVerifier.class);

    private static final OkHttpClient HTTP = new OkHttpClient.Builder()
            .connectTimeout(ofSeconds(3))
            .readTimeout(ofSeconds(1))
            .build();
    private final Clock clock;
    private final CentralRepository centralRepository;
    private final AppConfigAdapter appConfigAdapter;

    @Inject
    public ChallengeVerifier(
            final Clock clock,
            final CentralRepository centralRepository,
            final AppConfigAdapter appConfigAdapter
    ) {
        this.clock = clock;
        this.centralRepository = centralRepository;
        this.appConfigAdapter = appConfigAdapter;
    }

    public void verifyChallenges() {
        centralRepository.getAccounts().values().forEach(account -> account.getOrders().stream()
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
        if (request.getType().equals("dns") && challenge.getType().equals("http-01")) {
            return verifyHttpChallenge(account, request.getValue(), challenge);
        } else {
            throw new RuntimeException(format(
                    "Unknown request / challenge combination: %s/%s",
                    request.getType(),
                    challenge.getType()
            ));
        }
    }

    private boolean verifyHttpChallenge(
            final Account account,
            final String hostname,
            final AuthorizationChallenge challenge
    ) {
        LOG.info("Validating HTTP DNS challenge for {}", hostname);
        final String hostAndPort = hostname + ":" + appConfigAdapter.getHttpVerificationPort();
        final Request request = new Request.Builder()
                .url("http://" + hostAndPort + "/.well-known/acme-challenge/" + challenge.getToken())
                .build();
        final String expectedString = challenge.getToken() + "." + base64url(thumbprint(account));
        try {
            final Response response = HTTP.newCall(request).execute();
            try (final ResponseBody body = response.body()) {
                if (response.code() != 200) {
                    LOG.info("{} returned status code {}", hostname, response.code());
                    return false;
                }
                final String actualString = body.string();
                if (!expectedString.equals(actualString)) {
                    LOG.info("{} returned wrong value: {} instead of {}", hostname, actualString, expectedString);
                    return false;
                }
                LOG.info("HTTP DNS challenge successful for {}", hostname);
                return true;
            }
        } catch (final IOException e) {
            throw new RuntimeException(format(
                    "Failed to verify HTTP DNS challenge for %s",
                    hostname
            ), e);
        }
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
