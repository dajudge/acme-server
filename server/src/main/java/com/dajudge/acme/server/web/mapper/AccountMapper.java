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

package com.dajudge.acme.server.web.mapper;

import com.dajudge.acme.account.facade.transport.AccountTO;
import com.dajudge.acme.server.web.transport.AccountRTO;
import com.dajudge.acme.server.web.util.PathBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountMapper {
    private final PathBuilder pathBuilder;

    @Inject
    public AccountMapper(final PathBuilder pathBuilder) {
        this.pathBuilder = pathBuilder;
    }

    public AccountRTO toTransportObject(final AccountTO account) {
        final AccountRTO ret = new AccountRTO();
        ret.setContact(account.getContact());
        ret.setStatus("valid");
        ret.setOrders(pathBuilder.accountOrdersUrl(account.getId()).toString());
        return ret;
    }
}
