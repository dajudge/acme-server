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

import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class AccountTO {
    private final String id;
    private final List<String> contact;
    private final Map<String, Object> publicKey;

    public AccountTO(
            final String id,
            final List<String> contact,
            final Map<String, Object> publicKey
    ) {
        this.id = id;
        this.contact = contact;
        this.publicKey = publicKey;
    }

    public Map<String, Object> getPublicKey() {
        return publicKey;
    }

    public List<String> getContact() {
        return contact;
    }

    public String getId() {
        return id;
    }
}
