package com.dajudge.acme.server.web.provider.nonce;

import com.dajudge.acme.server.facade.NonceFacade;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

import static java.util.Arrays.stream;

@Provider
public class NonceInterceptor implements WriterInterceptor {
    @Inject
    NonceFacade nonceFacade;

    @Override
    public void aroundWriteTo(final WriterInterceptorContext context) throws IOException, WebApplicationException {
        if (isPostRequest(context)) {
            context.getHeaders().putSingle("Replay-Nonce", nonceFacade.newNonce());
        }
        context.proceed();
    }

    private boolean isPostRequest(final WriterInterceptorContext context) {
        return context.getAnnotations() != null && stream(context.getAnnotations())
                .anyMatch(it -> it.annotationType() == POST.class);
    }
}
