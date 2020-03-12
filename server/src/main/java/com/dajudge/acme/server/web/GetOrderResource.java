package com.dajudge.acme.server.web;

import com.dajudge.acme.server.facade.OrderFacade;
import com.dajudge.acme.server.transport.OrderTO;
import com.dajudge.acme.server.web.mapper.OrderMapper;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path(GetOrderResource.BASE_PATH)
public class GetOrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(GetOrderResource.class);
    public static final String BASE_PATH = "/acmev2/orders/{orderId}";
    private final OrderFacade orderFacade;
    private final OrderMapper orderMapper;

    @Inject
    public GetOrderResource(
            final OrderFacade orderFacade,
            final OrderMapper orderMapper
    ) {
        this.orderFacade = orderFacade;
        this.orderMapper = orderMapper;
    }

    @POST
    @Produces("application/json")
    public Response getOrder(
            @PathParam("orderId") final String orderId,
            JwsRequestRTO<Void> request
    ) {
        LOG.info("Get order: {}", orderId);
        final String kid = request.getProtectedPart().getKid();
        final String accountId = kid.substring(kid.lastIndexOf("/") + 1);
        final OrderTO order = orderFacade.getOrder(accountId, orderId);
        LOG.info("Returning order: {}", order);
        return Response.ok(orderMapper.toRest(order))
                .build();
    }
}
