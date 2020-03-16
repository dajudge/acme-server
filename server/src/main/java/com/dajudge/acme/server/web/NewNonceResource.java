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

import com.dajudge.acme.server.facade.NonceFacade;

import javax.inject.Inject;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path(NewNonceResource.BASE_PATH)
public class NewNonceResource {
    static final String BASE_PATH = "/acmev2/nonce";

    private final NonceFacade nonceFacade;

    @Inject
    public NewNonceResource(final NonceFacade nonceFacade) {
        this.nonceFacade = nonceFacade;
    }

    @HEAD
    public Response nonce() {
        return Response.ok()
                .header("Replay-Nonce", nonceFacade.newNonce())
                .header("Cache-Control", "no-store")
                .build();
    }
}
