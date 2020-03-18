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

import com.dajudge.acme.server.facade.OrderFacade;
import com.dajudge.acme.server.transport.OrderTO;
import com.dajudge.acme.server.web.mapper.OrderMapper;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;

@Path(GetOrderResource.BASE_PATH)
public class GetOrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(GetOrderResource.class);
    public static final String BASE_PATH = ACMEV2_PREFIX + "/order/{orderId}";
    private final OrderFacade orderFacade;
    private final OrderMapper orderMapper;
    private final PathBuilder pathBuilder;

    @Inject
    public GetOrderResource(
            final OrderFacade orderFacade,
            final OrderMapper orderMapper,
            final PathBuilder pathBuilder
    ) {
        this.orderFacade = orderFacade;
        this.orderMapper = orderMapper;
        this.pathBuilder = pathBuilder;
    }

    @POST
    @Produces("application/json")
    public Response getOrder(
            @PathParam("orderId") final String orderId,
            JwsRequestRTO<Void> request
    ) {
        LOG.info("Get order: {} {}", request.getAccountId(), orderId);
        final OrderTO order = orderFacade.getOrder(request.getAccountId(), orderId);
        LOG.info("Returning order: {}", order);
        return Response.ok(orderMapper.toRest(order))
                .build();
    }
}
