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

import com.dajudge.acme.server.facade.ConfigFacade;
import com.dajudge.acme.server.web.transport.DirectoryResponseRTO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path(DirectoryResource.BASE_PATH)
public class DirectoryResource {
    static final String BASE_PATH = "/directory";

    private final ConfigFacade configFacade;

    @Inject
    public DirectoryResource(final ConfigFacade configFacade) {
        this.configFacade = configFacade;
    }

    @GET
    @Produces("application/json")
    public DirectoryResponseRTO getDirectory() {
        return new DirectoryResponseRTO(
                configFacade.getServerBaseUrl() + NewNonceResource.BASE_PATH,
                configFacade.getServerBaseUrl() + NewAccountResource.BASE_PATH,
                configFacade.getServerBaseUrl() + NewOrderResource.BASE_PATH,
                "n/a",
                "n/a",
                "n/a"
        );
    }
}
