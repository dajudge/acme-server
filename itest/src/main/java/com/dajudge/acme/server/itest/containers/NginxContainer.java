package com.dajudge.acme.server.itest.containers;

import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Volume;
import org.testcontainers.containers.GenericContainer;

import static java.util.Collections.singletonList;

public class NginxContainer extends GenericContainer<NginxContainer> {
    public NginxContainer(final String volumeName) {
        super("nginx:1.17.8");
        setBinds(singletonList(new Bind(volumeName, new Volume("/usr/share/nginx/html"), AccessMode.ro)));
    }
}
