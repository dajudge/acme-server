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

import com.dajudge.acme.server.facade.AccountFacade;
import com.dajudge.acme.server.facade.ConfigFacade;
import com.dajudge.acme.server.transport.AccountTO;
import com.dajudge.acme.server.web.transport.CreateAccountRequestRTO;
import com.dajudge.acme.server.web.transport.CreateAccountResponseRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path(NewAccountResource.BASE_PATH)
public class NewAccountResource {
    private static final Logger LOG = LoggerFactory.getLogger(NewAccountResource.class);
    static final String BASE_PATH = "/acmev2/account";

    private final ConfigFacade configFacade;
    private final AccountFacade accountFacade;

    @Inject
    public NewAccountResource(
            final ConfigFacade configFacade,
            final AccountFacade accountFacade
    ) {
        this.configFacade = configFacade;
        this.accountFacade = accountFacade;
    }

    @POST
    @Produces("application/json")
    public Response createAccount(final JwsRequestRTO<CreateAccountRequestRTO> request) {
        LOG.info("New account request: {}", request.getPayload());
        final AccountTO account = accountFacade.createAccount(
                request.getPayload().getContact(),
                request.getProtectedPart().getJwk()
        );
        final CreateAccountResponseRTO response = new CreateAccountResponseRTO(
                "valid",
                account.getContact(),
                accountOrdersUrl(account)
        );
        return Response.created(accountUri(account))
                .entity(response)
                .build();
    }

    private URI accountUri(final AccountTO account) {
        try {
            return new URI(accountUrl(account));
        } catch (final URISyntaxException e) {
            throw new RuntimeException("WTF?", e);
        }
    }

    private String accountUrl(final AccountTO account) {
        return configFacade.getServerBaseUrl() + "/fixme/account/" + account.getId();
    }

    private String accountOrdersUrl(final AccountTO account) {
        return accountUrl(account) + "/orders";
    }
}
