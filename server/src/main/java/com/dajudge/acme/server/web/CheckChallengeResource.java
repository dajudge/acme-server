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

import com.dajudge.acme.account.facade.ChallengeFacade;
import com.dajudge.acme.account.facade.transport.ChallengeStatusCTO;
import com.dajudge.acme.server.web.transport.CheckChallengeResponseRTO;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;
import static java.util.Locale.US;

@Path(CheckChallengeResource.BASE_PATH)
public class CheckChallengeResource {
    private static final Logger LOG = LoggerFactory.getLogger(CheckChallengeResource.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    public static final String BASE_PATH = ACMEV2_PREFIX + "/challenges/{challengeId}";

    private final PathBuilder pathBuilder;
    private final ChallengeFacade challengeFacade;

    @Inject
    public CheckChallengeResource(
            final PathBuilder pathBuilder,
            final ChallengeFacade challengeFacade
    ) {
        this.pathBuilder = pathBuilder;
        this.challengeFacade = challengeFacade;
    }

    @POST
    @Produces("application/json")
    public Response checkChallenge(
            @PathParam("challengeId") final String challengeId,
            final JwsRequestRTO<Object> request
    ) {
        LOG.info("Check challenge request: {}", challengeId);
        final ChallengeStatusCTO status = challengeFacade.getChallengeStatus(challengeId);
        final CheckChallengeResponseRTO response = new CheckChallengeResponseRTO(
                status.getChallenge().getType(),
                pathBuilder.challengeUrl(challengeId).toString(),
                status.getChallenge().getToken(),
                status.getChallenge().getStatus().toString().toLowerCase(US),
                formatDateNullSafe(status.getChallenge().getValidated())
        );
        return Response.ok(response)
                .header("Link", "<" + pathBuilder.authUrl(status.getAuthorizationId()) + ">;rel=\"up\"")
                .build();
    }

    private String formatDateNullSafe(final Date date) {
        return date == null ? null : DATE_FORMAT.format(date);
    }


}
