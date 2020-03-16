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
