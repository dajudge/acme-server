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

import javax.inject.Singleton;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class SerialNumberFactory {
    private static final AtomicLong SERIAL = new AtomicLong();

    public BigInteger next() {
        return BigInteger.valueOf(SERIAL.incrementAndGet());
    }
}
