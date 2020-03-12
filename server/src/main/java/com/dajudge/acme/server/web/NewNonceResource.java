package com.dajudge.acme.server.web;

import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path(NewNonceResource.BASE_PATH)
public class NewNonceResource {
    static final String BASE_PATH = "/acmev2/nonce";

    @HEAD
    public Response nonce() {
        return Response.ok()
                .header("Replay-Nonce", UUID.randomUUID())
                .header("Cache-Control", "no-store")
                .build();
    }
}
