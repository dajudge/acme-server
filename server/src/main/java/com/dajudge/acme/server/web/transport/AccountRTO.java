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

import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class AccountRTO {
    private String status;
    private List<String> contact;
    private String orders;
    private Boolean termsOfServiceAgreed;
    private Boolean onlyReturnExisting;
    private Map<String, Object> externalAccountBinding;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<String> getContact() {
        return contact;
    }

    public void setContact(final List<String> contact) {
        this.contact = contact;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(final String orders) {
        this.orders = orders;
    }

    public Boolean getTermsOfServiceAgreed() {
        return termsOfServiceAgreed;
    }

    public void setTermsOfServiceAgreed(final Boolean termsOfServiceAgreed) {
        this.termsOfServiceAgreed = termsOfServiceAgreed;
    }

    public Boolean getOnlyReturnExisting() {
        return onlyReturnExisting;
    }

    public void setOnlyReturnExisting(final Boolean onlyReturnExisting) {
        this.onlyReturnExisting = onlyReturnExisting;
    }

    public Map<String, Object> getExternalAccountBinding() {
        return externalAccountBinding;
    }

    public void setExternalAccountBinding(final Map<String, Object> externalAccountBinding) {
        this.externalAccountBinding = externalAccountBinding;
    }
}
