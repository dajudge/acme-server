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

package com.dajudge.acme.ca.builtin;

import com.dajudge.acme.ca.CertificationAuthority;
import com.dajudge.acme.ca.Clock;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Singleton
public class BuiltinCertificationAuthority implements CertificationAuthority {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private static final AlgorithmIdentifier SIGNATURE_ALGORITHM = new DefaultSignatureAlgorithmIdentifierFinder()
            .find("SHA1withRSA");
    private static final AlgorithmIdentifier DIGEST_ALGORITHM = new DefaultDigestAlgorithmIdentifierFinder()
            .find(SIGNATURE_ALGORITHM);
    private static final CertificateFactory CERT_FACTORY = createCertificateFactory();

    private final KeyPair caKeyPair = createKeyPair();

    private final Clock clock;

    private final SerialNumberFactory serialNumberFactory;

    @Inject
    public BuiltinCertificationAuthority(
            final Clock clock,
            final SerialNumberFactory serialNumberFactory
    ) {
        this.clock = clock;
        this.serialNumberFactory = serialNumberFactory;
    }

    @Override
    public List<String> createCertificate(final byte[] csr) {
        try {
            final long validity = 30 * 365 * 24 * 60 * 60 * 1000;
            final PKCS10CertificationRequest csrHolder = parseCsr(csr);
            final X509Certificate cert = sign(csrHolder, validity);
            return Collections.singletonList(
                    encode(cert)
            );
        } catch (final IOException | OperatorCreationException | CertificateException e) {
            throw new RuntimeException("Error signing CSR", e);
        }
    }

    @Override
    public String getName() {
        return "builtin";
    }

    private static KeyPair createKeyPair() {
        try {
            final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create CA key pair", e);
        }
    }

    private static String encode(final X509Certificate cert) throws CertificateEncodingException {
        return Base64.getEncoder().encodeToString(cert.getEncoded());
    }

    private X509Certificate sign(
            final PKCS10CertificationRequest csr,
            final long validity
    ) throws IOException, OperatorCreationException, CertificateException {
        final AsymmetricKeyParameter caPrivateKey = PrivateKeyFactory.createKey(caKeyPair.getPrivate().getEncoded());
        final X509v3CertificateBuilder certGenerator = new X509v3CertificateBuilder(
                new X500Name("CN=issuer"),
                serialNumberFactory.next(),
                new Date(clock.now()),
                new Date(clock.now() + validity),
                csr.getSubject(),
                csr.getSubjectPublicKeyInfo()
        );

        final ContentSigner signer = new BcRSAContentSignerBuilder(SIGNATURE_ALGORITHM, DIGEST_ALGORITHM)
                .build(caPrivateKey);

        final X509CertificateHolder certHolder = certGenerator.build(signer);
        final Certificate certStructure = certHolder.toASN1Structure();

        try (final InputStream certStream = new ByteArrayInputStream(certStructure.getEncoded())) {
            return (X509Certificate) CERT_FACTORY.generateCertificate(certStream);
        }
    }

    private PKCS10CertificationRequest parseCsr(final byte[] csr) {
        try {
            return new PKCS10CertificationRequest(csr);
        } catch (final Exception e) {
            throw new RuntimeException("Error parsing CSR: " + csr, e);
        }
    }

    private static CertificateFactory createCertificateFactory() {
        try {
            return CertificateFactory.getInstance("X.509", "BC");
        } catch (final CertificateException | NoSuchProviderException e) {
            throw new RuntimeException("Error creating CA certificate factory", e);
        }
    }
}
