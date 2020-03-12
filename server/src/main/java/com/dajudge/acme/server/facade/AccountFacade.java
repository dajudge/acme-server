package com.dajudge.acme.server.facade;

import com.dajudge.acme.server.model.Account;
import com.dajudge.acme.server.repository.CentralRepository;
import com.dajudge.acme.server.transport.AccountTO;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Dependent
public class AccountFacade {
    private final CentralRepository centralRepository;

    @Inject
    public AccountFacade(final CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    public AccountTO createAccount(final List<String> contact) {
        final Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setContact(contact);
        centralRepository.getAccounts().put(account.getId(), account);
        return new AccountTO(account.getId(), account.getContact());
    }
}
