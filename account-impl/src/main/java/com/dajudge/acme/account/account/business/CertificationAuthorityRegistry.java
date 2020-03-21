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

package com.dajudge.acme.account.account.business;

import com.dajudge.acme.ca.CertificationAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Singleton
public class CertificationAuthorityRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(CertificationAuthorityRegistry.class);
    private final Map<String, CertificationAuthority> authorities;

    public CertificationAuthorityRegistry() {
        authorities = null;
    }

    @Inject
    public CertificationAuthorityRegistry(
            final @Any Instance<CertificationAuthority> authorities
    ) {
        this.authorities = authorities.stream()
                .collect(toMap(
                        CertificationAuthority::getName,
                        identity()
                ));
        LOG.info("Available CAs: {}", this.authorities.keySet());
    }

    public List<String> createCertificate(final byte[] csr) {
        return authorities.get("builtin").createCertificate(csr);
    }
}
