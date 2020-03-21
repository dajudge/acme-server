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
