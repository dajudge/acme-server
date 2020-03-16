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

package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class AuthorizationStatusCTO {
    private final AuthorizationRequestTO orderRequestIdentifier;
    private final AuthorizationTO authorization;
    private final AuthorizationStatusEnum status;

    public AuthorizationStatusCTO(
            final AuthorizationRequestTO orderRequestIdentifier,
            final AuthorizationTO authorization,
            final AuthorizationStatusEnum status
    ) {
        this.orderRequestIdentifier = orderRequestIdentifier;
        this.authorization = authorization;
        this.status = status;
    }

    public AuthorizationRequestTO getOrderRequestIdentifier() {
        return orderRequestIdentifier;
    }

    public AuthorizationTO getAuthorization() {
        return authorization;
    }

    public AuthorizationStatusEnum getStatus() {
        return status;
    }
}
