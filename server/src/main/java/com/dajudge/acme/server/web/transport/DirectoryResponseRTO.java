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
public class DirectoryResponseRTO {
    private final String newNonce;
    private final String newAccount;
    private final String newOrder;
    private final String newAuthz;
    private final String revokeCert;
    private final String keyChange;

    public DirectoryResponseRTO(
            final String newNonce,
            final String newAccount,
            final String newOrder,
            final String newAuthz,
            final String revokeCert,
            final String keyChange
    ) {
        this.newNonce = newNonce;
        this.newAccount = newAccount;
        this.newOrder = newOrder;
        this.newAuthz = newAuthz;
        this.revokeCert = revokeCert;
        this.keyChange = keyChange;
    }

    public String getNewNonce() {
        return newNonce;
    }

    public String getNewAccount() {
        return newAccount;
    }

    public String getNewOrder() {
        return newOrder;
    }

    public String getNewAuthz() {
        return newAuthz;
    }

    public String getRevokeCert() {
        return revokeCert;
    }

    public String getKeyChange() {
        return keyChange;
    }
}
