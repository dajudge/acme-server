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

package com.dajudge.acme.server.scheduler;

import com.dajudge.acme.server.facade.ChallengeVerificationFacade;
import io.quarkus.scheduler.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ChallengeVerificationScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(ChallengeVerificationScheduler.class);
    private final ChallengeVerificationFacade challengeVerificationFacade;

    @Inject
    public ChallengeVerificationScheduler(final ChallengeVerificationFacade challengeVerificationFacade) {
        this.challengeVerificationFacade = challengeVerificationFacade;
    }

    @Scheduled(every = "1s")
    public void verifyChallenges() {
        LOG.info("Running challenge verification scheduler...");
        try {
            challengeVerificationFacade.verifyChallenges();
        } catch (final Exception e) {
            LOG.info("Challenge verification error", e);
        }
    }
}
