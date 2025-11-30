package org.example.api;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.example.api.dto.EndpointRequest;
import org.example.api.dto.EndpointResponse;
import org.example.core.allure.Attach;
import org.example.core.allure.Steps;
import org.example.core.config.TestEnv;
import org.example.core.http.RestAssuredClient;
import org.example.core.step.EndpointStep;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class EndpointApi {
    private final TestEnv env;
    private final RestAssuredClient client;

    public EndpointResponse call(EndpointRequest req, String apiKey) {
        Response raw = EndpointStep.sendRequest(req, apiKey, env, client);
        return EndpointStep.parseResponse(raw);
    }
}
