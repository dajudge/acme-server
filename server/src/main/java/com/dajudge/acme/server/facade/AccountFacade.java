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

package com.dajudge.acme.server.facade;

import com.dajudge.acme.server.facade.mapper.AccountMapper;
import com.dajudge.acme.server.model.Account;
import com.dajudge.acme.server.repository.CentralRepository;
import com.dajudge.acme.server.transport.AccountTO;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class AccountFacade {
    private final CentralRepository centralRepository;

    @Inject
    public AccountFacade(final CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    public AccountTO createAccount(
            final List<String> contact,
            final Map<String, Object> publicKey
    ) {
        assert contact != null && !contact.isEmpty();
        assert publicKey != null;
        final Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setContact(contact);
        account.setPublicKey(publicKey);
        centralRepository.getAccounts().add(account);
        return AccountMapper.toTransportObject(account);
    }

    public Optional<AccountTO> findById(final String accountId) {
        return centralRepository.getAccounts().stream()
                .filter(it -> accountId.equals(it.getId()))
                .findAny()
                .map(AccountMapper::toTransportObject);
    }
}
