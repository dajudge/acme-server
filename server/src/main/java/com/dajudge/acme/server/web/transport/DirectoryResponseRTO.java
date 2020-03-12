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
