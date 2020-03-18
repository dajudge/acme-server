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
