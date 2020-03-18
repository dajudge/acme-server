package com.dajudge.acme.server.web.provider.exception;

import com.dajudge.acme.server.web.exception.AcmeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AcmeExceptionMapper implements ExceptionMapper<AcmeException> {
    private static final Logger LOG = LoggerFactory.getLogger(AcmeExceptionMapper.class);

    @Override
    public Response toResponse(final AcmeException exception) {
        final String type = exception.getClass().getSimpleName();
        final Response response = exception.getResponse();
        final Object entity = response.getEntity();
        LOG.info("{}({}): {}", type, response.getStatus(), entity);
        return response;
    }
}
