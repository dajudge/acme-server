package com.dajudge.acme.server;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;

@QuarkusTest
class CertbotIntegrationTest {
    @Inject
    private TestContainers testContainers;

    @Test
    public void happy_path_succeeds() {
        testContainers.certbot().certonly("www.example.com", "mail@" + UUID.randomUUID() + ".example.com");
    }
}