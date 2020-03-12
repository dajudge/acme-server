package com.dajudge.acme.server;

import com.dajudge.acme.server.itest.containers.CertbotContainer;
import com.dajudge.acme.server.itest.containers.NginxContainer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class TestContainers {
    private final String volumeName = UUID.randomUUID().toString();
    private final CertbotContainer certbot = new CertbotContainer(volumeName, this::getAcmeServer);
    private final NginxContainer nginx = new NginxContainer(volumeName);
    private final AcmeServerPortProvider portProvider;

    public TestContainers() {
        portProvider = null;
    }

    @Inject
    public TestContainers(final AcmeServerPortProvider portProvider) {
        this.portProvider = portProvider;
    }

    @PostConstruct
    public void startContainers() {
        nginx.start();
        certbot.start();
    }

    @PreDestroy
    public void stopContainers() {
        nginx.close();
        certbot.start();
    }

    private String getAcmeServer() {
        return getAcmeServerBaseAddress() + "/directory";
    }

    @NotNull
    public String getAcmeServerBaseAddress() {
        final String gatewayAddress = certbot.getContainerInfo().getNetworkSettings()
                .getNetworks().get("bridge")
                .getGateway();
        return "http://" + gatewayAddress + ":" + portProvider.get();
    }

    public CertbotContainer certbot() {
        return certbot;
    }

    public NginxContainer nginx() {
        return nginx;
    }

}
