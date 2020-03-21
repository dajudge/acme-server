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

package com.dajudge.acme.server.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;

public final class ProblemDocumentMatchers {
    private ProblemDocumentMatchers() {
    }

    public static Matcher<?> isProblemDocument(final String type) {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(final Object actual) {
                return type.equals(new JSONObject(actual.toString()).getString("type"));
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("problem document with type '" + type + "'");
            }
        };
    }
}
