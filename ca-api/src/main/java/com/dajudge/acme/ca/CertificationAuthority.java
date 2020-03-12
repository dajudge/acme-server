package com.dajudge.acme.ca;

import java.util.List;

public interface CertificationAuthority {
    List<String> createCertificate(final byte[] csr);

    String getName();
}
