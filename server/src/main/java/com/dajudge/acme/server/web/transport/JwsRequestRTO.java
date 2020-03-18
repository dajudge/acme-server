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

package com.dajudge.acme.server.web.transport;


import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class JwsRequestRTO<T> {
    private final JwsProtectedPartRTO protectedPart;
    private final T payload;
    private final String signature;
    private final String accountId;

    public JwsRequestRTO(
            final JwsProtectedPartRTO protectedPart,
            final T payload,
            final String signature,
            final String accountId) {
        this.protectedPart = protectedPart;
        this.payload = payload;
        this.signature = signature;
        this.accountId = accountId;
    }

    public T getPayload() {
        return payload;
    }

    public JwsProtectedPartRTO getProtectedPart() {
        return protectedPart;
    }

    public String getAccountId() {
        return accountId;
    }

    public static <T> JwsRequestRTO<T> create(
            final JwsProtectedPartRTO protectedPart,
            final Class<T> payloadClass,
            final T payload,
            final String signature,
            final String accountId
    ) {
        return new JwsRequestRTO<>(protectedPart, payload, signature, accountId);
    }
}
