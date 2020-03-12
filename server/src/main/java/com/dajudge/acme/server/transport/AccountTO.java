package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class AccountTO {
    private final String id;
    private final List<String> contact;

    public AccountTO(final String id, final List<String> contact) {
        this.id = id;
        this.contact = contact;
    }

    public List<String> getContact() {
        return contact;
    }

    public String getId() {
        return id;
    }
}
