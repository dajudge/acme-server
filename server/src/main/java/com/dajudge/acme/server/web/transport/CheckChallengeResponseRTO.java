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

package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class CheckChallengeResponseRTO{
    private final String type;
    private final String url;
    private final String token;
    private final String status;
    private final String validated;

    public CheckChallengeResponseRTO(
            final String type,
            final String url,
            final String token,
            final String status,
            final String validated
    ) {
        this.type = type;
        this.url = url;
        this.token = token;
        this.status = status;
        this.validated = validated;
    }

    public String getStatus() {
        return status;
    }

    public String getValidated() {
        return validated;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }
}
