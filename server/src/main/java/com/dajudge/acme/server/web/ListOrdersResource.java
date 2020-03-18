package com.dajudge.acme.server.web;

import com.dajudge.acme.server.facade.OrderFacade;
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
