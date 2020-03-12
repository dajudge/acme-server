package com.dajudge.acme.server.repository;

import com.dajudge.acme.server.model.Account;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class CentralRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    public Map<String, Account> getAccounts() {
        return accounts;
    }
}
