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
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.transport.ListOrdersResponseRTO;
import com.dajudge.acme.server.web.util.PathBuilder;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.dajudge.acme.server.web.ListOrdersResource.BASE_PATH;
import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;
import static java.util.stream.Collectors.toList;

@Path(BASE_PATH)
public class ListOrdersResource {
    public static final String BASE_PATH = ACMEV2_PREFIX + "/account/{accountId}/orders";

    private final OrderFacade orderFacade;
    private final PathBuilder pathBuilder;

    @Inject
    public ListOrdersResource(final OrderFacade orderFacade, final PathBuilder pathBuilder) {
        this.orderFacade = orderFacade;
        this.pathBuilder = pathBuilder;
    }

    @POST
    @Produces("application/json")
    public Response getOrdersList(
            final @PathParam("accountId") String accountId,
            final JwsRequestRTO<Void> request
    ) {
        if (!accountId.equals(request.getAccountId())) {
            return Response.status(404).build();
        }
        final List<String> links = orderFacade.listOrders(accountId).stream()
                .map(order -> pathBuilder.orderUrl(order.getId()).toString())
                .collect(toList());
        return Response.ok(new ListOrdersResponseRTO(links))
                .build();
    }
}
