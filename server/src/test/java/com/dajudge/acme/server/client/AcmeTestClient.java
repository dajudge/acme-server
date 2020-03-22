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

package com.dajudge.acme.server.client;

import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.Headers;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;

import java.security.Key;
import java.security.KeyPair;

import static io.restassured.RestAssured.given;
import static java.util.function.Function.identity;
import static org.hamcrest.Matchers.any;
import static org.jose4j.jws.AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256;
import static org.jose4j.jwx.CompactSerializer.deserialize;

public class AcmeTestClient {
    public String nextNonce = newNonce();

    public String newNonce() {
        return newNonce(any(String.class));
    }

    public String newNonce(final Matcher<String> matcher) {
        final String newNonceUrl = newNonceUrl();
        return given()
                .head(newNonceUrl)
                .then()
                .header("Replay-Nonce", identity(), matcher)
                .extract()
                .header("Replay-Nonce");
    }

    public CreateAccountRequest newAccount(final KeyPair keyPair, final JSONObject body) {
        final String url = newAccountUrl();
        return new CreateAccountRequest(request(keyPair, null, url, body.toString())
                .then(), this::setNextNonce);
    }

    public GetOrdersRequest getOrders(final KeyPair keyPair, final String kid, final String ordersUrl) {
        return new GetOrdersRequest(
                getAsPost(keyPair, kid, ordersUrl).then(),
                this::setNextNonce
        );
    }

    public GetAccountRequest getAccount(final KeyPair keyPair, final String kid) {
        return new GetAccountRequest(
                getAsPost(keyPair, kid, kid).then(),
                this::setNextNonce
        );
    }

    private Response getAsPost(final KeyPair keyPair, final String kid, final String url) {
        return request(keyPair, kid, url, "");
    }

    private Response request(final KeyPair keyPair, final String kid, final String url, final String payload) {
        return given()
                .body(jws(keyPair, url, kid, payload))
                .contentType("application/jose+json")
                .post(url);
    }

    private static JSONObject toJson(final Headers headers) {
        return new JSONObject(headers.getFullHeaderAsJsonString());
    }


    private void setNextNonce(final String nextNonce) {
        this.nextNonce = nextNonce;
    }

    private String jws(final KeyPair keyPair, final String url, final String kid, final String body) {
        try {
            final JsonWebSignature jws = createSignature(keyPair, url, kid, body);
            final String[] encodedParts = deserialize(jws.getCompactSerialization());
            final JSONObject jwsObject = new JSONObject();
            jwsObject.put("protected", encodedParts[0]);
            jwsObject.put("payload", encodedParts[1]);
            jwsObject.put("signature", encodedParts[2]);
            return jwsObject.toString();
        } catch (final JoseException e) {
            throw new RuntimeException("Failed to encapsulate message as JWS", e);
        }
    }

    private JsonWebSignature createSignature(
            final KeyPair keyPair,
            final String url,
            final String kid,
            final String body
    ) throws JoseException {
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(body);
        jws.setAlgorithmHeaderValue(ECDSA_USING_P256_CURVE_AND_SHA256);
        jws.setKey(keyPair.getPrivate());
        jws.getHeaders().setFullHeaderAsJsonString(headerAsJson(keyPair, url, kid, jws).toString());
        jws.sign();
        return jws;
    }

    private JSONObject headerAsJson(
            final KeyPair keyPair,
            final String url,
            final String kid,
            final JsonWebSignature jws
    ) {
        final JSONObject headers = toJson(jws.getHeaders());
        headers.put("kid", kid);
        headers.put("jwk", kid != null ? null : jwk(keyPair.getPublic()));
        headers.put("nonce", nextNonce);
        headers.put("url", url);
        return headers;
    }

    private JSONObject jwk(final Key key) {
        try {
            return new JSONObject(JsonWebKey.Factory.newJwk(key).toJson());
        } catch (final JoseException e) {
            throw new RuntimeException("Failed to create serialized JWK from key");
        }
    }

    private String newNonceUrl() {
        return directoryEntryFor("newNonce");
    }

    private String newAccountUrl() {
        return directoryEntryFor("newAccount");
    }

    private String directoryEntryFor(final String urlName) {
        return given().get("/directory")
                .then().statusCode(200)
                .and().contentType("application/json")
                .extract().body().jsonPath().get(urlName);
    }
}
