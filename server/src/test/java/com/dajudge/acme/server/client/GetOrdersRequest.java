package com.dajudge.acme.server.client;

import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

import java.util.function.Consumer;

public class GetOrdersRequest extends BasePostRequest {
    public GetOrdersRequest(final ValidatableResponse validatableResponse, final Consumer<String> nextNonceConsumer) {
        super(validatableResponse, nextNonceConsumer);
    }

public JSONObject succeeds() {
        return super.succeeds(r -> r.statusCode(200));
}
}
