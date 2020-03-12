package com.dajudge.acme.server.facade.mapper;

import com.dajudge.acme.server.model.Order;
import com.dajudge.acme.server.transport.OrderTO;

public class OrderMapper {
    public static OrderTO toTransportObject(final Order order) {
        return new OrderTO(
                order.getId(),
                order.getStatus(),
                order.getExpires(),
                AuthorizationRequestMapper.toTransportObjects(order.getIdentifiers()),
                order.getCertificateChain()
        );
    }
}
