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
