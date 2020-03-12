package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@EqualsAndHashCode
public class OrderTO {
    private final String id;
    private final OrderStatusEnum status;
    private final Date expires;
    private final List<AuthorizationRequestTO> identifiers;
    private final List<String> certificateChain;

    public OrderTO(
            final String id,
            final OrderStatusEnum status,
            final Date expires,
            final List<AuthorizationRequestTO> identifiers,
            final List<String> certificateChain
    ) {
        this.id = id;
        this.status = status;
        this.expires = expires;
        this.identifiers = identifiers;
        this.certificateChain = certificateChain;
    }

    public Date getExpires() {
        return expires;
    }

    public List<AuthorizationRequestTO> getIdentifiers() {
        return identifiers;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public List<String> getCertificateChain() {
        return certificateChain;
    }
}
