package com.dajudge.acme.server.business;

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
