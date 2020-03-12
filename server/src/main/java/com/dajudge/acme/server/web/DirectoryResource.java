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
