package com.dajudge.acme.server;

import io.restassured.RestAssured;

import javax.enterprise.context.Dependent;

@Dependent
public class RestAssuredPortProvider implements AcmeServerPortProvider {
    @Override
    public Integer get() {
        return RestAssured.port;
    }
}
