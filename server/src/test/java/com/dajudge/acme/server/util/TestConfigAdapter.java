package com.dajudge.acme.server.util;

import com.dajudge.acme.server.adapter.AppConfigAdapter;
import io.quarkus.arc.AlternativePriority;
import io.restassured.RestAssured;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

public class TestConfigAdapter {
    @Produces
    @AlternativePriority(1)
    @Dependent
    public AppConfigAdapter configFacade() {
        return new AppConfigAdapter() {
            @Override
            public String getServerBaseUrl() {
                return "http://localhost:" + RestAssured.port;
            }
        };
    }
}
