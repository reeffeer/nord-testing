package org.example.core.http;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.example.core.allure.Attach;
import org.example.core.config.TestEnv;

@Slf4j
public class RestAssuredClient {
    private final TestEnv env;

    public RestAssuredClient(TestEnv env) {
        this.env = env;
    }

    @Step("Создание базовой спецификации HTTP клиента для URL: {baseUrl}")
    public RequestSpecification baseSpec(String baseUrl) {
        Attach.text("Конфигурация HTTP клиента", 
            String.format("Base URL: %s\nAccept: application/json\nContent-Type: application/x-www-form-urlencoded\nHTTPS Validation: %s\nConnection Timeout: %d ms\nSocket Timeout: %d ms", 
                baseUrl, env.isRelaxedHttpsValidation() ? "relaxed" : "strict", 
                env.getHttpConnectionTimeout(), env.getHttpSocketTimeout()));
        
        RequestSpecification spec = RestAssured.given()
                .baseUri(baseUrl)
                .header("Accept", "application/json")
                .contentType("application/x-www-form-urlencoded");
        
        if (env.isRelaxedHttpsValidation()) {
            spec.relaxedHTTPSValidation();
        }
        
        return spec;
    }
}
