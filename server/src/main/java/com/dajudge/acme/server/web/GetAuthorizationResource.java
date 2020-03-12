package com.dajudge.acme.server.web;

import com.dajudge.acme.server.facade.AuthorizationFacade;
import com.dajudge.acme.server.facade.ConfigFacade;
import com.dajudge.acme.server.transport.AuthorizationStatusCTO;
import com.dajudge.acme.server.web.transport.AuthorizationChallengeRTO;
import com.dajudge.acme.server.web.transport.GetAuthorizationResponseRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.transport.OrderRequestIdentifierRTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static java.util.Locale.US;
import static java.util.stream.Collectors.toList;

@Path(GetAuthorizationResource.BASE_PATH)
public class GetAuthorizationResource {
    private static final Logger LOG = LoggerFactory.getLogger(GetAuthorizationResource.class);
    public static final String BASE_PATH = "/acmev2/authz/{authId}";

    private final ConfigFacade configFacade;
    private final AuthorizationFacade authorizationFacade;

    @Inject
    public GetAuthorizationResource(
            final ConfigFacade configFacade,
            final AuthorizationFacade authorizationFacade
    ) {
        this.configFacade = configFacade;
        this.authorizationFacade = authorizationFacade;
    }

    @POST
    @Produces("application/json")
    public Response getAuthorization(
            @PathParam("authId") final String authId,
            JwsRequestRTO<Void> request
    ) {
        LOG.info("Get authorization: {}", authId);
        final AuthorizationStatusCTO status = authorizationFacade.getAuthorizationById(authId);

        final List<AuthorizationChallengeRTO> challenges = status.getAuthorization().getChallenges().stream()
                .map(challenge -> new AuthorizationChallengeRTO(
                        challenge.getType(),
                        challengeUrl(challenge.getId()),
                        challenge.getToken()
                ))
                .collect(toList());
        final GetAuthorizationResponseRTO response = new GetAuthorizationResponseRTO(
                new OrderRequestIdentifierRTO(
                        status.getOrderRequestIdentifier().getType(),
                        status.getOrderRequestIdentifier().getValue()
                ),
                status.getStatus().toString().toLowerCase(US),
                challenges);
        LOG.info("Get authorization response: {}", response);
        return Response.ok(response)
                .header("Replay-Nonce", UUID.randomUUID())
                .build();
    }

    private String challengeUrl(final String challengeId) {
        return configFacade.getServerBaseUrl() + CheckChallengeResource.BASE_PATH.replace("{challengeId}", challengeId);
    }
}
