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

import com.dajudge.acme.server.web.transport.DirectoryResponseRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path(DirectoryResource.BASE_PATH)
public class DirectoryResource {
    @ConfigProperty
    static final String BASE_PATH = "/directory";

    private final PathBuilder pathBuilder;

    @Inject
    public DirectoryResource(final PathBuilder pathBuilder) {
        this.pathBuilder = pathBuilder;
    }

    @GET
    @Produces("application/json")
    public DirectoryResponseRTO getDirectory() {
        return new DirectoryResponseRTO(
                pathBuilder.getServerBaseUrl() + NewNonceResource.BASE_PATH,
                pathBuilder.getServerBaseUrl() + NewAccountResource.BASE_PATH,
                pathBuilder.getServerBaseUrl() + NewOrderResource.BASE_PATH,
                "n/a",
                "n/a",
                "n/a"
        );
    }
}
