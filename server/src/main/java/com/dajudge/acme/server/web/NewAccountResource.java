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
import java.util.UUID;

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
                request.getPayload().getContact()
        );
        final CreateAccountResponseRTO response = new CreateAccountResponseRTO(
                "valid",
                account.getContact(),
                accountOrdersUrl(account)
        );
        return Response.created(accountUri(account))
                .entity(response)
                .header("Replay-Nonce", UUID.randomUUID().toString())
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
