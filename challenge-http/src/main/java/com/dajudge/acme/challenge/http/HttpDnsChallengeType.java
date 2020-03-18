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

package com.dajudge.acme.challenge.http;

import com.dajudge.acme.challenge.ChallengeType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

import static com.dajudge.acme.common.util.StringUtils.base64url;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;

@Singleton
public class HttpDnsChallengeType implements ChallengeType {
    private static final Logger LOG = LoggerFactory.getLogger(HttpDnsChallengeType.class);
    private static final OkHttpClient HTTP = new OkHttpClient.Builder()
            .connectTimeout(ofSeconds(3))
            .readTimeout(ofSeconds(1))
            .build();
    private final Config config;

    public interface Config {
        int getVerificationPort();
    }

    @Singleton
    public static class DefaultConfig implements Config {
        @Override
        public int getVerificationPort() {
            return 80;
        }
    }

    @Inject
    public HttpDnsChallengeType(
            final Config config
    ) {
        this.config = config;
    }

    @Override
    public String getIdentifierType() {
        return "dns";
    }

    @Override
    public String getChallengeType() {
        return "http-01";
    }

    @Override
    public boolean verifyChallenge(
            final String hostname,
            final String token,
            final byte[] accountKeyThumbprint
    ) {
        LOG.info("Validating HTTP DNS challenge for {}", hostname);
        final String hostAndPort = hostname + ":" + config.getVerificationPort();
        final Request request = new Request.Builder()
                .url("http://" + hostAndPort + "/.well-known/acme-challenge/" + token)
                .build();
        final String expectedString = token + "." + base64url(accountKeyThumbprint);
        try {
            final Response response = HTTP.newCall(request).execute();
            try (final ResponseBody body = response.body()) {
                if (response.code() != 200) {
                    LOG.info("{} returned status code {}", hostname, response.code());
                    return false;
                }
                final String actualString = body.string();
                if (!expectedString.equals(actualString)) {
                    LOG.info("{} returned wrong value: {} instead of {}", hostname, actualString, expectedString);
                    return false;
                }
                LOG.info("HTTP DNS challenge successful for {}", hostname);
                return true;
            }
        } catch (final IOException e) {
            throw new RuntimeException(format(
                    "Failed to verify HTTP DNS challenge for %s",
                    hostname
            ), e);
        }
    }
}
