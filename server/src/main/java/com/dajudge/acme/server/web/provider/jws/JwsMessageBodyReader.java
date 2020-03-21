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

package com.dajudge.acme.server.web.provider.jws;

import com.dajudge.acme.account.facade.AccountFacade;
import com.dajudge.acme.account.facade.transport.AccountTO;
import com.dajudge.acme.nonce.facade.NonceFacade;
import com.dajudge.acme.server.web.transport.JwsProtectedPartRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.dajudge.acme.server.web.util.PathBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.jose4j.base64url.Base64;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dajudge.acme.server.web.exception.BadNonceException.badNonce;
import static com.dajudge.acme.server.web.exception.MalformedRequestException.noJsonWebKeyOrKeyIdentifier;
import static com.dajudge.acme.server.web.exception.UnauthorizedException.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.jose4j.jwk.JsonWebKey.Factory.newJwk;
import static org.jose4j.jwx.CompactSerializer.serialize;

@Provider
public class JwsMessageBodyReader implements MessageBodyReader<JwsRequestRTO> {
    private static final Logger LOG = LoggerFactory.getLogger(JwsMessageBodyReader.class);
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Inject
    NonceFacade nonceFacade;
    @Inject
    PathBuilder pathBuilder;
    @Inject
    AccountFacade accountFacade;

    @Override
    public boolean isReadable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType
    ) {
        if (!mediaType.getType().equals("application") || !mediaType.getSubtype().equals("jose+json")) {
            return false;
        }
        if (JwsRequestRTO.class != type) {
            return false;
        }
        if (!(genericType instanceof ParameterizedType)) {
            return false;
        }
        final ParameterizedType parameterizedType = (ParameterizedType) genericType;
        final Type payloadType = parameterizedType.getActualTypeArguments()[0];
        return payloadType instanceof Class;
    }

    @Override
    public JwsRequestRTO readFrom(
            final Class<JwsRequestRTO> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType,
            final MultivaluedMap<String, String> httpHeaders,
            final InputStream entityStream
    ) throws IOException, WebApplicationException {
        try {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            final Class<?> payloadClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            final byte[] payloadBytes = IOUtils.toByteArray(entityStream);
            final JwsPayload jwsPayload = JSON_MAPPER.readValue(
                    payloadBytes,
                    JwsPayload.class
            );
            final JwsProtectedPartRTO jwsProtectedPart = JSON_MAPPER.readValue(
                    Base64.decode(jwsPayload.getProtected()),
                    JwsProtectedPartRTO.class
            );
            LOG.info("Protected part: {}", jwsProtectedPart);
            if (!nonceFacade.validate(jwsProtectedPart.getNonce())) {
                throw badNonce(jwsProtectedPart.getNonce());
            }
            if (jwsProtectedPart.getKid() == null && jwsProtectedPart.getJwk() == null) {
                throw noJsonWebKeyOrKeyIdentifier();
            }
            final Optional<String> accountId = pathBuilder.accountIdFromKey(jwsProtectedPart.getKid());
            if (accountId.isPresent()) {
                final Optional<AccountTO> account = accountFacade.findById(accountId.get());
                if (!account.isPresent()) {
                    throw unknownKeyId(jwsProtectedPart.getKid());
                }
                final Map<String, Object> pubkey = account.get().getPublicKey();
                LOG.info("Public key for {}: {}", jwsProtectedPart.getKid(), pubkey);
                final JsonWebSignature jws = new JsonWebSignature();
                jws.setCompactSerialization(serialize(
                        jwsPayload.getProtected(),
                        jwsPayload.getPayload(),
                        jwsPayload.getSignature()
                ));
                jws.setKey(newJwk(pubkey).getKey());
                if (!jws.verifySignature()) {
                    throw signatureVerificationFailed();
                }
            } else {
                if (!allowWithoutKeyId(annotations)) {
                    throw noKeyIdProvided();
                }
            }
            if (payloadClass == Void.class) {
                return createRequestObject(
                        jwsProtectedPart,
                        jwsPayload.getSignature(),
                        accountId.orElse(null)
                );
            }
            return createRequestObject(
                    jwsProtectedPart,
                    payloadClass,
                    Base64.decode(jwsPayload.getPayload()),
                    jwsPayload.getSignature(),
                    accountId.orElse(null)
            );
        } catch (final JsonParseException | JsonMappingException | JoseException e) {
            throw new WebApplicationException("Failed to deserialize JWS request body", e, BAD_REQUEST);
        }
    }

    private JwsRequestRTO createRequestObject(
            final JwsProtectedPartRTO protectedPart,
            final String signature,
            final String accountId
    ) {
        return JwsRequestRTO.create(
                protectedPart,
                Void.class,
                null,
                signature,
                accountId
        );
    }

    private <T> JwsRequestRTO<T> createRequestObject(
            final JwsProtectedPartRTO protectedPart,
            final Class<T> payloadClass,
            final byte[] payloadJson,
            final String signature,
            final String accountId
    ) throws IOException {
        return JwsRequestRTO.create(
                protectedPart,
                payloadClass,
                JSON_MAPPER.readValue(
                        payloadJson,
                        payloadClass
                ),
                signature,
                accountId
        );
    }

    private boolean allowWithoutKeyId(final Annotation[] annotations) {
        return annotations != null &&
                Stream.of(annotations).anyMatch(a -> a.annotationType() == AllowWithoutKeyId.class);
    }
}
