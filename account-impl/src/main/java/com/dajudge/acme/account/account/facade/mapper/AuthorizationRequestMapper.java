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

package com.dajudge.acme.account.account.facade.mapper;

import com.dajudge.acme.account.account.model.AuthorizationRequest;
import com.dajudge.acme.account.facade.transport.AuthorizationRequestTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AuthorizationRequestMapper {
    public static List<AuthorizationRequestTO> toTransportObjects(final List<AuthorizationRequest> identifiers) {
        return identifiers.stream()
                .map(it -> new AuthorizationRequestTO(it.getId(), it.getType(), it.getValue()))
                .collect(toList());
    }
}
