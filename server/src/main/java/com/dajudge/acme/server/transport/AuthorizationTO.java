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

package com.dajudge.acme.server.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@ToString
@EqualsAndHashCode
public class AuthorizationTO {
    private final Collection<AuthorizationChallengeTO> challenges;
    private final String id;

    public AuthorizationTO(
            final String id,
            final Collection<AuthorizationChallengeTO> challenges
    ) {
        this.id = id;
        this.challenges = challenges;
    }

    public Collection<AuthorizationChallengeTO> getChallenges() {
        return challenges;
    }

    public String getId() {
        return id;
    }
}
