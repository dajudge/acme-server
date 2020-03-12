package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class CreateAccountResponseRTO {
    private final String status;
    private final List<String> contact;
    private final String orders;

    public CreateAccountResponseRTO(
            final String status,
            final List<String> contact,
            final String orders
    ) {
        this.status = status;
        this.contact = contact;
        this.orders = orders;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getContact() {
        return contact;
    }

    public String getOrders() {
        return orders;
    }
}
