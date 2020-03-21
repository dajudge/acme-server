package com.dajudge.acme.server.client;

import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

import java.util.function.Consumer;

import static org.hamcrest.Matchers.notNullValue;

public class NewAccountRequest extends BasePostRequest {
    public NewAccountRequest(
            final ValidatableResponse validatableResponse,
            final Consumer<String> nextNonceConsumer
    ) {
        super(validatableResponse, nextNonceConsumer);
    }

    public JSONObject succeeds() {
        return super.succeeds(r -> r.statusCode(201).header("Location", notNullValue()));
    }
}
