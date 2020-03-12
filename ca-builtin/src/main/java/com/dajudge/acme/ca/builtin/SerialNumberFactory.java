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
