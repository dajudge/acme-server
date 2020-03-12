package com.dajudge.acme.server.facade;

import javax.enterprise.context.Dependent;
import java.util.UUID;

@Dependent
public class NonceFacade {
    public boolean validate(final String nonce) {
        return true;
    }

    public String newNonce() {
        return UUID.randomUUID().toString();
    }
}
