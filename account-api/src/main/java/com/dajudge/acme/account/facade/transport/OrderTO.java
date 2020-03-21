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

package com.dajudge.acme.account.facade.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@EqualsAndHashCode
public class OrderTO {
    private final String id;
    private final OrderStatusEnum status;
    private final Date expires;
    private final List<AuthorizationRequestTO> identifiers;
    private final List<String> certificateChain;

    public OrderTO(
            final String id,
            final OrderStatusEnum status,
            final Date expires,
            final List<AuthorizationRequestTO> identifiers,
            final List<String> certificateChain
    ) {
        this.id = id;
        this.status = status;
        this.expires = expires;
        this.identifiers = identifiers;
        this.certificateChain = certificateChain;
    }

    public Date getExpires() {
        return expires;
    }

    public List<AuthorizationRequestTO> getIdentifiers() {
        return identifiers;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public List<String> getCertificateChain() {
        return certificateChain;
    }
}
