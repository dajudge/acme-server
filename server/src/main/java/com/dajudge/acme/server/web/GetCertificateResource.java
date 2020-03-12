package com.dajudge.acme.server.web;

import com.dajudge.acme.server.facade.OrderFacade;
import com.dajudge.acme.server.transport.OrderTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

@Path(GetCertificateResource.BASE_PATH)
public class GetCertificateResource {
    private final String PREFIX = "-----BEGIN CERTIFICATE-----";
    private final String SUFFIX = "-----END CERTIFICATE-----";
    public static final String BASE_PATH = "/acmev2/cert/{orderId}";
    private final OrderFacade orderFacade;

    @Inject
    public GetCertificateResource(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @POST
    @Produces("application/pem-certificate-chain")
    public Response getCertificate(
            @PathParam("orderId") final String orderId,
            JwsRequestRTO<Void> request
    ) {
        final String kid = request.getProtectedPart().getKid();
        final String accountId = kid.substring(kid.lastIndexOf("/") + 1);
        final OrderTO order = orderFacade.getOrder(accountId, orderId);
        final String chain = order.getCertificateChain().stream()
                .map(cert -> PREFIX + "\n" + cert + "\n" + SUFFIX)
                .collect(joining("\n"));
        return Response.ok(chain.getBytes(UTF_8))
                .header("Replay-Nonce", UUID.randomUUID())
                .build();
    }
}
