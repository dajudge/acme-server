package com.dajudge.acme.server.facade.mapper;

import com.dajudge.acme.server.model.AuthorizationRequest;
import com.dajudge.acme.server.transport.AuthorizationRequestTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AuthorizationRequestMapper {
    public static List<AuthorizationRequestTO> toTransportObjects(final List<AuthorizationRequest> identifiers) {
        return identifiers.stream()
                .map(it -> new AuthorizationRequestTO(it.getId(), it.getType(), it.getValue()))
                .collect(toList());
    }
}
