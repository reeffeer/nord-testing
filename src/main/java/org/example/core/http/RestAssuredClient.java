package org.example.core.http;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAssuredClient {

    public RequestSpecification baseSpec(String baseUrl) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Accept", "application/json")
                .contentType("application/x-www-form-urlencoded")
                .relaxedHTTPSValidation();
    }
}
