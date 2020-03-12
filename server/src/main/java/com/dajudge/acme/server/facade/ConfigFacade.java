package com.dajudge.acme.server.facade;

import com.dajudge.acme.server.adapter.AppConfigAdapter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ConfigFacade {
    private final AppConfigAdapter appConfig;

    @Inject
    public ConfigFacade(final AppConfigAdapter appConfig) {
        this.appConfig = appConfig;
    }

    public String getServerBaseUrl() {
        return appConfig.getServerBaseUrl();
    }
}
