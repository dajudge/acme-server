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
import com.dajudge.acme.account.facade.transport.OrderTO;
import com.dajudge.acme.server.web.mapper.OrderMapper;
import com.dajudge.acme.server.web.transport.FinalizeOrderRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import org.jose4j.base64url.Base64Url;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;

@Path(FinalizeOrderResource.BASE_PATH)
public class FinalizeOrderResource {
    public static final String BASE_PATH = ACMEV2_PREFIX + "/orders/{orderId}/finalize";

    private final OrderMapper orderMapper;
    private final OrderFacade orderFacade;
    private final PathBuilder pathBuilder;

    @Inject
    public FinalizeOrderResource(
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
    public Response finalizeOrder(
            final @PathParam("orderId") String orderId,
            final JwsRequestRTO<FinalizeOrderRTO> request
    ) {
        final String csr = request.getPayload().getCsr();
        final OrderTO order = orderFacade.finalizeOrder(orderId, Base64Url.decode(csr));
        return Response.ok(orderMapper.toRest(order))
                .location(pathBuilder.orderUrl(order.getId()))
                .build();
    }
}
