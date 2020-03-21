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
