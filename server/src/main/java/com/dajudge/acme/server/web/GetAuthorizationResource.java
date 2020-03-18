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

import com.dajudge.acme.server.facade.AuthorizationFacade;
import com.dajudge.acme.server.transport.AuthorizationStatusCTO;
import com.dajudge.acme.server.web.transport.AuthorizationChallengeRTO;
import com.dajudge.acme.server.web.transport.GetAuthorizationResponseRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.transport.OrderRequestIdentifierRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;
import static java.util.Locale.US;
import static java.util.stream.Collectors.toList;

@Path(GetAuthorizationResource.BASE_PATH)
public class GetAuthorizationResource {
    private static final Logger LOG = LoggerFactory.getLogger(GetAuthorizationResource.class);
    public static final String BASE_PATH = ACMEV2_PREFIX + "/authz/{authId}";

    private final AuthorizationFacade authorizationFacade;
    private final PathBuilder pathBuilder;

    @Inject
    public GetAuthorizationResource(
            final AuthorizationFacade authorizationFacade,
            final PathBuilder pathBuilder
    ) {
        this.authorizationFacade = authorizationFacade;
        this.pathBuilder = pathBuilder;
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
                        pathBuilder.challengeUrl(challenge.getId()).toString(),
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
                .build();
    }
}
