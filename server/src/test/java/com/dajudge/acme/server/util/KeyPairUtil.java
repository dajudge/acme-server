package com.dajudge.acme.server.util;

import org.jose4j.keys.EcKeyUtil;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;

import java.security.KeyPair;

public final class KeyPairUtil {
    private KeyPairUtil() {
    }

    public static KeyPair generateKeyPair() {
        try {
            return new EcKeyUtil().generateKeyPair(EllipticCurves.P256);
        } catch (final JoseException e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }
}
