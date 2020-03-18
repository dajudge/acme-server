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

import com.dajudge.acme.server.facade.OrderFacade;
import com.dajudge.acme.server.transport.OrderTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.dajudge.acme.server.web.util.PathBuilder.ACMEV2_PREFIX;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@Path(GetCertificateResource.BASE_PATH)
public class GetCertificateResource {
    private final String PREFIX = "-----BEGIN CERTIFICATE-----";
    private final String SUFFIX = "-----END CERTIFICATE-----";
    public static final String BASE_PATH = ACMEV2_PREFIX + "/cert/{orderId}";
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
                .build();
    }
}
