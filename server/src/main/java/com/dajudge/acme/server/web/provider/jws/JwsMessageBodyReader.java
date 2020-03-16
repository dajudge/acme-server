package com.dajudge.acme.server.web.provider.jws;

import com.dajudge.acme.server.facade.NonceFacade;
import com.dajudge.acme.server.web.exception.BadNonceException;
import com.dajudge.acme.server.web.transport.JwsProtectedPartRTO;
import com.dajudge.acme.server.web.transport.JwsRequestRTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.jose4j.base64url.Base64;
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

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class JwsMessageBodyReader implements MessageBodyReader<JwsRequestRTO> {
    private static final Logger LOG = LoggerFactory.getLogger(JwsMessageBodyReader.class);
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Inject
    NonceFacade nonceFacade;

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
            final JwsPayload jws = JSON_MAPPER.readValue(
                    payloadBytes,
                    JwsPayload.class
            );
            final JwsProtectedPartRTO jwsProtectedPart = JSON_MAPPER.readValue(
                    Base64.decode(jws.getProtected()),
                    JwsProtectedPartRTO.class
            );
            if (!nonceFacade.validate(jwsProtectedPart.getNonce())) {
                throw new BadNonceException(jwsProtectedPart.getNonce());
            }
            if (payloadClass == Void.class) {
                return createRequestObject(
                        jwsProtectedPart,
                        jws.getSignature()
                );
            }
            return createRequestObject(
                    jwsProtectedPart,
                    payloadClass,
                    Base64.decode(jws.getPayload()),
                    jws.getSignature()
            );
        } catch (final JsonParseException | JsonMappingException e) {
            throw new WebApplicationException("Failed to deserialize JWS request body", e, BAD_REQUEST);
        }
    }

    private JwsRequestRTO createRequestObject(
            final JwsProtectedPartRTO protectedPart,
            final String signature
    ) {
        return JwsRequestRTO.create(protectedPart, Void.class, null, signature);
    }

    private <T> JwsRequestRTO<T> createRequestObject(
            final JwsProtectedPartRTO protectedPart,
            final Class<T> payloadClass,
            final byte[] payloadJson,
            final String signature
    ) throws IOException {
        return JwsRequestRTO.create(
                protectedPart,
                payloadClass,
                JSON_MAPPER.readValue(
                        payloadJson,
                        payloadClass
                ),
                signature
        );
    }
}
