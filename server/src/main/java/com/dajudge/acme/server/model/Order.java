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

package com.dajudge.acme.server.model;

import com.dajudge.acme.server.transport.OrderStatusEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private Date expires;
    private OrderStatusEnum status;
    private List<AuthorizationRequest> identifiers = new ArrayList<>();
    private List<String> certificateChain;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(final Date expires) {
        this.expires = expires;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(final OrderStatusEnum status) {
        this.status = status;
    }

    public List<AuthorizationRequest> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(final List<AuthorizationRequest> identifiers) {
        this.identifiers = identifiers;
    }

    public List<String> getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(final List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }
}
