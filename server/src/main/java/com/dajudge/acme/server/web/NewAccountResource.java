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

import com.dajudge.acme.account.facade.AccountFacade;
import com.dajudge.acme.account.facade.transport.AccountTO;
import com.dajudge.acme.server.web.provider.jws.AllowWithoutKeyId;
import com.dajudge.acme.server.web.transport.CreateAccountRequestRTO;
import com.dajudge.acme.server.web.transport.CreateAccountResponseRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;

@Path(NewAccountResource.BASE_PATH)
public class NewAccountResource {
    private static final Logger LOG = LoggerFactory.getLogger(NewAccountResource.class);
    static final String BASE_PATH = ACMEV2_PREFIX + "/account";

    private final AccountFacade accountFacade;
    private final PathBuilder pathBuilder;

    @Inject
    public NewAccountResource(
            final AccountFacade accountFacade,
            final PathBuilder pathBuilder
    ) {
        this.accountFacade = accountFacade;
        this.pathBuilder = pathBuilder;
    }

    @POST
    @Produces("application/json")
    public Response createAccount(final @AllowWithoutKeyId JwsRequestRTO<CreateAccountRequestRTO> request) {
        LOG.info("New account request: {}", request.getPayload());
        LOG.info("Protected part: {}", request.getProtectedPart());
        final AccountTO account = accountFacade.createAccount(
                request.getPayload().getContact(),
                request.getProtectedPart().getJwk()
        );
        final CreateAccountResponseRTO response = new CreateAccountResponseRTO(
                "valid",
                account.getContact(),
                pathBuilder.accountOrdersUrl(account.getId()).toString()
        );
        return Response.created(pathBuilder.accountUrl(account.getId()))
                .entity(response)
                .build();
    }
}
