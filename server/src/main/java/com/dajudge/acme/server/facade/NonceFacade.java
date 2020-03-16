package com.dajudge.acme.server.facade;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.synchronizedSet;

@Singleton
public class NonceFacade {
    private final Set<String> validNonces = synchronizedSet(new HashSet<>());

    public boolean validate(final String nonce) {
        return validNonces.remove(nonce);
    }

    public String newNonce() {
        final String nonce = UUID.randomUUID().toString();
        validNonces.add(nonce);
        return nonce;
    }
}
