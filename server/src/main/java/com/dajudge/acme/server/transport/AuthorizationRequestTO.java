package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class AuthorizationRequestTO {
    private final String id;
    private final String type;
    private final String value;

    public AuthorizationRequestTO(final String id, final String type, final String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
