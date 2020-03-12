package com.dajudge.acme.server.adapter;

import com.dajudge.acme.ca.Clock;

import javax.inject.Singleton;

@Singleton
public class SystemClock implements Clock {
    @Override
    public long now() {
        return System.currentTimeMillis();
    }
}
