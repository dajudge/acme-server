package com.dajudge.acme.server.facade.mapper;

import com.dajudge.acme.server.model.Account;
import com.dajudge.acme.server.transport.AccountTO;

public final class AccountMapper {
    private AccountMapper() {
    }

    public static AccountTO toTransportObject(final Account account) {
        return new AccountTO(account.getId(), account.getContact(), account.getPublicKey());
    }
}
