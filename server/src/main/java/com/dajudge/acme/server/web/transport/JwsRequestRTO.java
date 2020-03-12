package com.dajudge.acme.server.web.transport;


import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class JwsRequestRTO<T> {
    private final JwsProtectedPartRTO protectedPart;
    private final T payload;
    private final String signature;

    public JwsRequestRTO(
            final JwsProtectedPartRTO protectedPart,
            final T payload,
            final String signature
    ) {
        this.protectedPart = protectedPart;
        this.payload = payload;
        this.signature = signature;
    }

    public T getPayload() {
        return payload;
    }

    public JwsProtectedPartRTO getProtectedPart() {
        return protectedPart;
    }

    public static <T> JwsRequestRTO<T> create(
            final JwsProtectedPartRTO protectedPart,
            final Class<T> payloadClass,
            final T payload,
            final String signature
    ) {
        return new JwsRequestRTO<>(protectedPart, payload, signature);
    }
}
