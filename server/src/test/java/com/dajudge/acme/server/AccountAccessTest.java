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

import io.quarkus.test.junit.QuarkusTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static com.dajudge.acme.server.util.AccountRequestBuilder.accountRequestObject;
import static com.dajudge.acme.server.util.KeyPairUtil.generateKeyPair;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AccountAccessTest {
    private AcmeServer acmeServer;

    @BeforeEach
    public void init() {
        acmeServer = new AcmeServer();
    }

    @Test
    public void blocks_request_with_wrong_signature() {
        final KeyPair keyPairA = generateKeyPair();
        final JSONObject account = acmeServer.newAccount(keyPairA, accountRequestObject("test@example.com"));
        final String kid = account.getString("_location");
        final KeyPair keyPairB = generateKeyPair();
        acmeServer.getOrdersRequest(keyPairB, kid, account.getString("orders"))
                .statusCode(404);
    }

    @Test
    public void allows_request_with_correct_signature() {
        final KeyPair keyPair = generateKeyPair();
        final JSONObject account = acmeServer.newAccount(keyPair, accountRequestObject("test@example.com"));
        final String kid = account.getString("_location");
        assertNotNull(acmeServer.getOrders(keyPair, kid, account.getString("orders")));
    }
}