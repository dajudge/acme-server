package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class AuthorizationStatusCTO {
    private final AuthorizationRequestTO orderRequestIdentifier;
    private final AuthorizationTO authorization;
    private final AuthorizationStatusEnum status;

    public AuthorizationStatusCTO(
            final AuthorizationRequestTO orderRequestIdentifier,
            final AuthorizationTO authorization,
            final AuthorizationStatusEnum status
    ) {
        this.orderRequestIdentifier = orderRequestIdentifier;
        this.authorization = authorization;
        this.status = status;
    }

    public AuthorizationRequestTO getOrderRequestIdentifier() {
        return orderRequestIdentifier;
    }

    public AuthorizationTO getAuthorization() {
        return authorization;
    }

    public AuthorizationStatusEnum getStatus() {
        return status;
    }
}
