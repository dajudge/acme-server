package com.dajudge.acme.server.adapter;

import com.dajudge.acme.server.TestContainers;
import io.quarkus.arc.AlternativePriority;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TestAppConfigAdapter {
    @Dependent
    @Produces
    @AlternativePriority(1)
    public AppConfigAdapter getAppConfigAdapter(final TestContainers testContainers) {
        return new AppConfigAdapter() {
            @Override
            public String getServerBaseUrl() {
                return testContainers.getAcmeServerBaseAddress();
            }
        };
    }
}
