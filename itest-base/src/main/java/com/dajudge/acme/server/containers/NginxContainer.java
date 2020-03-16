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

package com.dajudge.acme.server.containers;

import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Volume;
import org.testcontainers.containers.GenericContainer;

import static java.util.Collections.singletonList;
import static org.testcontainers.shaded.com.google.common.primitives.Ints.asList;

public class NginxContainer extends GenericContainer<NginxContainer> {
    public NginxContainer(final String volumeName) {
        super("nginx:1.17.8");
        setExposedPorts(asList(80));
        setBinds(singletonList(new Bind(volumeName, new Volume("/usr/share/nginx/html"), AccessMode.ro)));
    }
}
