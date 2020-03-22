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

package com.dajudge.acme.server.web;

import com.dajudge.acme.account.facade.OrderFacade;
import com.dajudge.acme.account.facade.transport.AuthorizationRequestTO;
import com.dajudge.acme.account.facade.transport.OrderTO;
import com.dajudge.acme.server.web.mapper.OrderMapper;
import com.dajudge.acme.server.web.transport.CreateOrderRequestRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.transport.OrderRequestIdentifierRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;
import static java.util.stream.Collectors.toList;

@Path(CreateOrderResource.BASE_PATH)
public class CreateOrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(CreateOrderResource.class);
    static final String BASE_PATH = ACMEV2_PREFIX + "/orders";

    private final OrderMapper orderMapper;
    private final OrderFacade orderFacade;
    private final PathBuilder pathBuilder;

    @Inject
    public CreateOrderResource(
            final OrderMapper orderMapper,
            final OrderFacade orderFacade,
            final PathBuilder pathBuilder
    ) {
        this.orderMapper = orderMapper;
        this.orderFacade = orderFacade;
        this.pathBuilder = pathBuilder;
    }

    @POST
    @Produces("application/json")
    public Response createOrder(final JwsRequestRTO<CreateOrderRequestRTO> request) {
        LOG.info("New order request: {}", request.getPayload());
        final OrderTO order = orderFacade.createOrder(
                request.getAccountId(),
                toIdentifierTOs(request.getPayload().getIdentifiers())
        );
        LOG.info("Created order: {}", order);
        return Response.created(pathBuilder.orderUrl(order.getId()))
                .entity(orderMapper.toRest(order))
                .build();
    }

    private List<AuthorizationRequestTO> toIdentifierTOs(final List<OrderRequestIdentifierRTO> identifiers) {
        return identifiers.stream()
                .map(identifier -> new AuthorizationRequestTO(
                        null, // FIXME really ugly to pass this down here
                        identifier.getType(),
                        identifier.getValue()
                )).collect(toList());
    }
}
