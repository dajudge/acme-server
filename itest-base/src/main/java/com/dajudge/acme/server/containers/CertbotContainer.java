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
import org.testcontainers.shaded.com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class CertbotContainer extends GenericContainer<CertbotContainer> {
    private static final String WEBROOT_PATH = "/webroot";
    private final Supplier<String> acmeServer;

    public CertbotContainer(
            final String volumeName,
            final Supplier<String> acmeServer
    ) {
        super("certbot/certbot:v1.2.0");
        this.acmeServer = acmeServer;
        withCreateContainerCmdModifier(cmd -> {
            cmd.withEntrypoint();
            cmd.withCmd("sleep", "600");
        });
        setBinds(singletonList(new Bind(volumeName, new Volume(WEBROOT_PATH), AccessMode.rw)));
    }

    public void certonly(final String domain, final String email) {
        exec("certbot", "-n", "certonly",
                "-d", domain,
                "--webroot", "--webroot-path", WEBROOT_PATH,
                "--agree-tos", "--email", email,
                "--server", acmeServer.get());
    }

    private void exec(final String... cmd) {
        final String cmdString = join(" ", asList(cmd));
        System.out.println("$ " + cmdString);
        try {
            final ExecResult result = execInContainer(cmd);
            System.out.println("STDOUT:\n" + result.getStdout());
            System.out.println("STDERR:\n" + result.getStderr());
            System.out.println("LOG:\n" + getLetsencryptLog());
            if (result.getExitCode() != 0) {
                throw new RuntimeException("Command failed with exit code " + result.getExitCode() + ": " + cmdString);
            }
        } catch (final IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute command: " + cmdString);
        }
    }

    private String getLetsencryptLog() {
        return copyFileFromContainer("/var/log/letsencrypt/letsencrypt.log", is -> new ByteSource() {
            @Override
            public InputStream openStream() {
                return is;
            }
        }.asCharSource(ISO_8859_1).read());
    }
}
