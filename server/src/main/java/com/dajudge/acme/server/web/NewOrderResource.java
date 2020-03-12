package com.dajudge.acme.server.web;

import com.dajudge.acme.server.facade.OrderFacade;
import com.dajudge.acme.server.transport.AuthorizationRequestTO;
import com.dajudge.acme.server.transport.OrderTO;
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

import static java.util.stream.Collectors.toList;

@Path(NewOrderResource.BASE_PATH)
public class NewOrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(NewOrderResource.class);
    static final String BASE_PATH = "/acmev2/orders";

    private final OrderMapper orderMapper;
    private final OrderFacade orderFacade;
    private final PathBuilder pathBuilder;

    @Inject
    public NewOrderResource(
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
        final String kid = request.getProtectedPart().getKid();
        final String accountId = kid.substring(kid.lastIndexOf("/") + 1);
        final OrderTO order = orderFacade.createOrder(accountId, toIdentifierTOs(request.getPayload().getIdentifiers()));
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
