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

import java.util.function.Function;

import static com.dajudge.acme.server.util.ProblemDocumentMatchers.isProblemDocument;

public class BaseRequest {
    private final ValidatableResponse validatableResponse;

    public BaseRequest(final ValidatableResponse validatableResponse) {
        this.validatableResponse = validatableResponse;
    }

    public ValidatableResponse then() {
        return validatableResponse;
    }

    public void failsWith(final int statusCode, final String reason) {
        then().statusCode(statusCode).body(isProblemDocument(reason));
    }

    public JSONObject succeeds(
            final Function<ValidatableResponse, ValidatableResponse> checks
    ) {
        return processResponse(checks.apply(then()));
    }

    private JSONObject processResponse(final ValidatableResponse result) {
        final ExtractableResponse<Response> extract = result.extract();
        processExtracted(extract);
        final JSONObject resultObject = new JSONObject(extract.body().asString());
        resultObject.put("_location", extract.header("Location"));
        return resultObject;
    }

    protected void processExtracted(final ExtractableResponse<Response> extract) {
        // Hook for subclasses
    }
}
