package com.dajudge.acme.server.adapter;

import javax.enterprise.context.Dependent;

@Dependent
public class AppConfigAdapter {
    public String getServerBaseUrl() {
        return System.getenv("SERVER_BASE_URL");
    }
}
