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

package com.dajudge.acme.account.account.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Account {
    private String id;
    private List<String> contact;
    private List<Order> orders = new ArrayList<Order>();
    private Map<String, Object> publicKey;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<String> getContact() {
        return contact;
    }

    public void setContact(final List<String> contact) {
        this.contact = contact;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Map<String, Object> getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final Map<String, Object> publicKey) {
        this.publicKey = publicKey;
    }
}
