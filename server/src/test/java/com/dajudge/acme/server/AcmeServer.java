package com.dajudge.acme.server;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;
import org.jose4j.base64url.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.Headers;
import org.jose4j.keys.EcKeyUtil;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;

import java.security.KeyPair;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Function.identity;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.notNullValue;

class AcmeServer {
    public String nextNonce = newNonce();
    public KeyPair keyPair = generateKeyPair();

    public String newNonce() {
        return newNonce(any(String.class));
    }

    public String newNonce(final Matcher<String> matcher) {
        return given()
                .head(newNonceUrl())
                .then()
                .header("Replay-Nonce", identity(), matcher)
                .extract()
                .header("Replay-Nonce");
    }

    public JSONObject newAccount(final JSONObject body) {
        final ValidatableResponse response = newAccountRequest(body);
        final ExtractableResponse<Response> result = response
                .statusCode(201)
                .header("Location", notNullValue())
                .header("Replay-Nonce", notNullValue())
                .extract();
        nextNonce = result.header("Replay-Nonce");
        final JSONObject resultObject = new JSONObject(result.body().asString());
        resultObject.put("_location", result.header("Location"));
        return resultObject;
    }

    public ValidatableResponse newAccountRequest(final JSONObject body) {
        final String url = newAccountUrl();
        return given()
                .body(jws(url, body.toString()))
                .contentType("application/jose+json")
                .post(url)
                .then();
    }

    private String jws(final String url, final String body) {
        try {
            final JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(body);
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
            jws.setKey(keyPair.getPrivate());
            final JSONObject jwsObject = new JSONObject();
            jws.sign();
            jws.setKey(keyPair.getPublic());
            jwsObject.put("payload", Base64.encode(jws.getPayload().getBytes(UTF_8)));
            jwsObject.put("signature", jws.getEncodedSignature());
            final JSONObject headers = toJson(jws.getHeaders());
            headers.put("nonce", nextNonce);
            headers.put("url", url);
            jwsObject.put("protected", Base64.encode(headers.toString().getBytes(UTF_8)));
            return jwsObject.toString();
        } catch (final JoseException e) {
            throw new RuntimeException("Failed to encapsulate message as JWS", e);
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

    private static JSONObject toJson(final Headers headers) {
        return new JSONObject(headers.getFullHeaderAsJsonString());
    }


    public static KeyPair generateKeyPair() {
        try {
            return new EcKeyUtil().generateKeyPair(EllipticCurves.P256);
        } catch (final JoseException e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }
}
