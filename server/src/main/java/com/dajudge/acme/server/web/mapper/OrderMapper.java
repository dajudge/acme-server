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

package com.dajudge.acme.server.web.mapper;

import com.dajudge.acme.account.facade.transport.AuthorizationRequestTO;
import com.dajudge.acme.account.facade.transport.OrderTO;
import com.dajudge.acme.server.web.transport.OrderRTO;
import com.dajudge.acme.server.web.transport.OrderRequestIdentifierRTO;
import com.dajudge.acme.server.web.util.PathBuilder;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.util.Locale.US;
import static java.util.stream.Collectors.toList;

@Dependent
public class OrderMapper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private final PathBuilder pathBuilder;

    @Inject
    public OrderMapper(final PathBuilder pathBuilder) {
        this.pathBuilder = pathBuilder;
    }

    public OrderRTO toRest(final OrderTO order) {
        return new OrderRTO(
                order.getStatus().name().toLowerCase(US),
                DATE_FORMAT.format(order.getExpires()),
                toIdentiferRTOs(order.getIdentifiers()),
                toAuthorizationUrls(order.getIdentifiers()),
                pathBuilder.finalizeLinkForOrder(order.getId()).toString(),
                pathBuilder.certificateLinkForOrder(order.getId()).toString()
        );
    }


    private List<String> toAuthorizationUrls(final List<AuthorizationRequestTO> authorizations) {
        return authorizations.stream()
                .map(it -> pathBuilder.authUrl(it.getId()).toString())
                .collect(toList());
    }

    private List<OrderRequestIdentifierRTO> toIdentiferRTOs(final List<AuthorizationRequestTO> identifiers) {
        return identifiers.stream()
                .map(it -> new OrderRequestIdentifierRTO(
                        it.getType(),
                        it.getValue()
                ))
                .collect(toList());
    }
}
