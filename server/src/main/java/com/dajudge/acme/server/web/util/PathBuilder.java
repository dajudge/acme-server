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

package com.dajudge.acme.server.web.util;

import com.dajudge.acme.server.facade.ConfigFacade;
import com.dajudge.acme.server.web.*;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

@Dependent
public class PathBuilder {
    private final ConfigFacade configFacade;

    @Inject
    public PathBuilder(final ConfigFacade configFacade) {
        this.configFacade = configFacade;
    }

    public URI challengeUrl(final String challengeId) {
        return safeUrl(CheckChallengeResource.BASE_PATH.replace("{challengeId}", challengeId));
    }

    public URI authUrl(final String authId) {
        return safeUrl(GetAuthorizationResource.BASE_PATH.replace("{authId}", authId));
    }

    public URI finalizeLinkForOrder(final String id) {
        return safeUrl(FinalizeOrderResource.BASE_PATH.replace("{orderId}", id));
    }

    public URI certificateLinkForOrder(final String id) {
        return safeUrl(GetCertificateResource.BASE_PATH.replace("{orderId}", id));
    }

    public URI orderUrl(final String id) {
        return safeUrl(GetOrderResource.BASE_PATH.replace("{orderId}", id));
    }

    private URI safeUrl(final String path) {
        try {
            return new URI(configFacade.getServerBaseUrl() + path);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
