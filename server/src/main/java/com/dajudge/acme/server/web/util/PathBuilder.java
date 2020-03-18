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

import com.dajudge.acme.server.web.*;
import com.dajudge.acme.server.web.transport.JwsProtectedPartRTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.dajudge.acme.server.web.exception.UnauthorizedException.unknownKeyId;

@Dependent
public class PathBuilder {
    public static final String PROP_BASE_URL = "acme.server.baseUrl";
    public static final String ACMEV2_PREFIX = "/acmev2";
    private final Config config;

    public Optional<String> accountIdFromKey(final JwsProtectedPartRTO protectedPart) {
        final String kid = protectedPart.getKid();
        if (kid == null) {
            return Optional.empty();
        }
        final String urlPrefix = getServerBaseUrl() + GetAccountResource.BASE_PATH.replace("{accountId}", "");
        if (!kid.startsWith(urlPrefix)) {
            throw unknownKeyId(kid);
        }
        return Optional.of(kid.substring(urlPrefix.length()));
    }

    public interface Config {
        String getBaseUrl();
    }

    @Dependent
    public static class DefaultConfig implements Config {
        private final String baseUrl;

        @Inject
        public DefaultConfig(final @ConfigProperty(name = PROP_BASE_URL, defaultValue = "") String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Override
        public String getBaseUrl() {
            return baseUrl;
        }

    }

    @Inject
    public PathBuilder(final Config config) {
        this.config = config;
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

    public URI accountUrl(final String id) {
        return safeUrl(GetAccountResource.BASE_PATH.replace("{accountId}", id));
    }

    public URI accountOrdersUrl(final String id) {
        return safeUrl(ListOrdersResource.BASE_PATH.replace("{accountId}", id));
    }

    private URI safeUrl(final String path) {
        try {
            return new URI(getServerBaseUrl() + path);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServerBaseUrl() {
        return config.getBaseUrl();
    }
}
