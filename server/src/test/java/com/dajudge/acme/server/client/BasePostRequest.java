package com.dajudge.acme.server.client;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.hamcrest.Matchers.notNullValue;

public class BasePostRequest extends BaseRequest {

    private final Consumer<String> nextNonceConsumer;

    public BasePostRequest(
            final ValidatableResponse validatableResponse,
            final Consumer<String> nextNonceConsumer
    ) {
        super(validatableResponse);
        this.nextNonceConsumer = nextNonceConsumer;
    }

    public JSONObject succeeds(
            final Function<ValidatableResponse, ValidatableResponse> checks
    ) {
        return super.succeeds(r -> checks.apply(r).header("Replay-Nonce", notNullValue()));
    }

    @Override
    protected void processExtracted(final ExtractableResponse<Response> extract) {
        nextNonceConsumer.accept(extract.header("Replay-Nonce"));
    }
}
