/*
 * Copyright 2020 Alex Stockinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.dajudge.acme.server.facade.mapper;

import com.dajudge.acme.server.model.Order;
import com.dajudge.acme.server.transport.OrderTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public static List<OrderTO> toTransportObjects(final List<Order> orders) {
        return orders.stream().map(OrderMapper::toTransportObject).collect(toList());
    }
}
