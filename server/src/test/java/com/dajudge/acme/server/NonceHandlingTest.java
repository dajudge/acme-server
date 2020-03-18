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

package com.dajudge.acme.server;

import com.dajudge.acme.server.util.ProblemDocumentMatchers;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.UUID;

import static com.dajudge.acme.server.util.AccountRequestBuilder.accountRequestObject;
import static com.dajudge.acme.server.util.KeyPairUtil.generateKeyPair;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class NonceHandlingTest {

    private AcmeServer acmeServer;
    public KeyPair keyPair = generateKeyPair();


    @BeforeEach
    public void init() {
        acmeServer = new AcmeServer();
    }

    @Test
    public void returns_nonce() {
        acmeServer.newNonce(not(emptyOrNullString()));
    }

    @Test
    public void returns_unique_nonces() {
        final String firstNonce = acmeServer.newNonce();
        acmeServer.newNonce(not(equalTo(firstNonce)));
    }

    @Test
    public void accepts_provided_nonce() {
        acmeServer.newAccount(keyPair, accountRequestObject("mailto:test@example.com"));
    }

    @Test
    public void rejects_used_nonce() {
        final String oldNonce = acmeServer.nextNonce;
        acmeServer.newAccount(keyPair, accountRequestObject("mailto:test1@example.com"));
        acmeServer.nextNonce = oldNonce;
        acmeServer.newAccountRequest(keyPair, accountRequestObject("mailto:test2@example.com"))
                .statusCode(400)
                .body(ProblemDocumentMatchers.isProblemDocument("urn:ietf:params:acme:error:badNonce"));
    }

    @Test
    public void rejects_unknown_nonce() {
        acmeServer.nextNonce = UUID.randomUUID().toString();
        acmeServer.newAccountRequest(keyPair, accountRequestObject("mailto:test4@example.com"))
                .statusCode(400)
                .body(ProblemDocumentMatchers.isProblemDocument("urn:ietf:params:acme:error:badNonce"));
    }
}
