package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class CreateOrderRequestRTO {
    private List<OrderRequestIdentifierRTO> identifiers;

    public List<OrderRequestIdentifierRTO> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(final List<OrderRequestIdentifierRTO> identifiers) {
        this.identifiers = identifiers;
    }
}
