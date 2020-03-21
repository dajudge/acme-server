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

package com.dajudge.acme.server.web.provider.nonce;

import com.dajudge.acme.nonce.facade.NonceFacade;

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
