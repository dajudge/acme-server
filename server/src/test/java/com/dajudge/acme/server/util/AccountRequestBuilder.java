package com.dajudge.acme.server.util;

import org.json.JSONArray;
import org.json.JSONObject;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public final class AccountRequestBuilder {
    private AccountRequestBuilder() {
    }

    public static JSONObject accountRequestObject(final String accountEmail) {
        final JSONObject requestBody = new JSONObject();
        requestBody.put("contact", new JSONArray(asList(accountEmail)));
        return requestBody;
    }
}
