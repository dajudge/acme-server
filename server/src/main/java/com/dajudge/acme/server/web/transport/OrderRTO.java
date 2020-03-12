package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class OrderRTO {
    private final String status;
    private final String expires;
    private final List<OrderRequestIdentifierRTO> identifiers;
    private final List<String> authorizations;
    private final String finalize;
    private final String certificate;

    public OrderRTO(
            final String status,
            final String expires,
            final List<OrderRequestIdentifierRTO> identifiers,
            final List<String> authorizations,
            final String finalize,
            final String certificate
    ) {
        this.status = status;
        this.expires = expires;
        this.identifiers = identifiers;
        this.authorizations = authorizations;
        this.finalize = finalize;
        this.certificate = certificate;
    }

    public String getStatus() {
        return status;
    }

    public String getExpires() {
        return expires;
    }

    public List<OrderRequestIdentifierRTO> getIdentifiers() {
        return identifiers;
    }

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public String getFinalize() {
        return finalize;
    }

    public String getCertificate() {
        return certificate;
    }
}
