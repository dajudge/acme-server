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

package com.dajudge.acme.server;

import com.dajudge.acme.challenge.http.HttpDnsChallengeType;
import com.dajudge.acme.server.containers.CertbotContainer;
import com.dajudge.acme.server.containers.NginxContainer;
import com.dajudge.acme.server.web.util.PathBuilder;
import io.quarkus.arc.AlternativePriority;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private String getAcmeServerBaseAddress() {
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

    public String getNginxHostSuffix() {
        try {
            return InetAddress.getByName(nginx.getContainerIpAddress()).getHostAddress() + ".nip.io";
        } catch (final UnknownHostException e) {
            throw new RuntimeException("Failed to resolve IP of nginx container", e);
        }
    }

    @Produces
    @AlternativePriority(1)
    @Singleton
    public HttpDnsChallengeType.Config getHttpDnsChallengeConfig() {
        return () -> nginx.getMappedPort(80);
    }

    @Produces
    @AlternativePriority(1)
    @Singleton
    public PathBuilder.Config getPathBuilderConfig() {
        return () -> getAcmeServerBaseAddress();
    }
}
