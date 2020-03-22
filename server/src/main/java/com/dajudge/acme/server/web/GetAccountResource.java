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
import com.dajudge.acme.server.web.mapper.AccountMapper;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;

@Path(GetAccountResource.BASE_PATH)
public class GetAccountResource {
    public static final String BASE_PATH = ACMEV2_PREFIX + "/account/{accountId}";

    private final AccountFacade accountFacade;
    private final AccountMapper accountMapper;

    @Inject
    public GetAccountResource(
            final AccountFacade accountFacade,
            final AccountMapper accountMapper
    ) {
        this.accountFacade = accountFacade;
        this.accountMapper = accountMapper;
    }

    @POST
    @Produces("application/json")
    public Response getAccount(
            final @PathParam("accountId") String accountId,
            final JwsRequestRTO<Void> request
    ) {
        if (!accountId.equals(request.getAccountId())) {
            return Response.status(404).build();
        }
        final Optional<AccountTO> account = accountFacade.findById(accountId);
        if (!account.isPresent()) {
            return Response.status(404).build();
        }
        return Response.ok(accountMapper.toTransportObject(account.get())).build();
    }
}
