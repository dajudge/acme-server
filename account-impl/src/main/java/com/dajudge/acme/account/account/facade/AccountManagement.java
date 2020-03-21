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

package com.dajudge.acme.account.account.facade;

import com.dajudge.acme.account.facade.AccountFacade;
import com.dajudge.acme.account.facade.transport.AccountTO;
import com.dajudge.acme.account.account.facade.mapper.AccountMapper;
import com.dajudge.acme.account.account.model.Account;
import com.dajudge.acme.account.account.repository.CentralRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class AccountManagement implements AccountFacade {
    private final CentralRepository centralRepository;

    @Inject
    public AccountManagement(final CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    @Override
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

    @Override
    public Optional<AccountTO> findById(final String accountId) {
        return centralRepository.getAccounts().stream()
                .filter(it -> accountId.equals(it.getId()))
                .findAny()
                .map(AccountMapper::toTransportObject);
    }
}
