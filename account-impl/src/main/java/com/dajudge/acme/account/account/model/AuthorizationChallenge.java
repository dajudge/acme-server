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

package com.dajudge.acme.account.account.model;

import com.dajudge.acme.account.facade.transport.ChallengeStatusEnum;

import java.util.Date;

public class AuthorizationChallenge {
    private String id;
    private String type;
    private String token;
    private ChallengeStatusEnum status;
    private Date validated;

    public void setId(final String id) {
        this.id = id;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setStatus(final ChallengeStatusEnum status) {
        this.status = status;
    }

    public void setValidated(final Date validated) {
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
