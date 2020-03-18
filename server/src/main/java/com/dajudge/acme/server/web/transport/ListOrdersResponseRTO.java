package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class ListOrdersResponseRTO {
    private final List<String> orders;

    public ListOrdersResponseRTO(final List<String> orders) {
        this.orders = orders;
    }

    public List<String> getOrders() {
        return orders;
    }
}
