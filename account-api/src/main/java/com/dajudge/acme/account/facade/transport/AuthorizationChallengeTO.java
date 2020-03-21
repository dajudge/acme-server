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

package com.dajudge.acme.account.facade.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@ToString
@EqualsAndHashCode
public class AuthorizationChallengeTO {
    private final String id;
    private final String type;
    private final String token;
    private final ChallengeStatusEnum status;
    private final Date validated;

    public AuthorizationChallengeTO(
            final String id,
            final String type,
            final String token,
            final ChallengeStatusEnum status,
            final Date validated
    ) {
        this.id = id;
        this.type = type;
        this.token = token;
        this.status = status;
        this.validated = validated;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public ChallengeStatusEnum getStatus() {
        return status;
    }

    public Date getValidated() {
        return validated;
    }
}
