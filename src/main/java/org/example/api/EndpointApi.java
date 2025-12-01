package org.example.api;


import io.qameta.allure.Step;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.example.api.dto.EndpointRequest;
import org.example.api.dto.EndpointResponse;
import org.example.core.allure.Attach;
import org.example.core.config.TestEnv;
import org.example.core.http.RestAssuredClient;
import wiremock.org.eclipse.jetty.io.WriterOutputStream;

import java.io.PrintStream;
import java.io.StringWriter;


@RequiredArgsConstructor
public class EndpointApi {
    private final TestEnv env;
    private final RestAssuredClient client;

    @Step("Вызов эндпоинта /endpoint: action = {request.action}, X-Api-Key = {apiKey}")
    public EndpointResponse call(EndpointRequest request, String apiKey) {
        RequestSpecification spec = buildSpec(request, apiKey);

        StringWriter writer = new StringWriter();
        PrintStream ps = new PrintStream(new WriterOutputStream(writer), true);
        spec.filter(new RequestLoggingFilter(LogDetail.ALL, ps));

        Response response = spec.post("/endpoint").then().extract().response();

        Attach.text("Отправленный HTTP запрос", writer.toString());
        Attach.text("HTTP статус-код ответа", String.valueOf(response.getStatusCode()));
        Attach.json("Тело ответа (JSON)", response.asString());
        
        return parseResponse(response);
    }

    @Step("Парсинг ответа от сервиса")
    private EndpointResponse parseResponse(Response response) {
        String payload = response.asString();
        String result = payload.contains("\"OK\"") ? "OK" : "ERROR";
        String message = payload.contains("message") ? response.jsonPath().getString("message") : null;
        
        EndpointResponse parsedResponse = new EndpointResponse(result, message, response);
        
        Attach.text("Распарсенный результат", 
            String.format("Result: %s\nMessage: %s", result, message != null ? message : "null"));
        
        return parsedResponse;
    }

    @Step("Построение спецификации запроса: action = {req.action}, token = {req.token}, apiKey = {apiKey}")
    private RequestSpecification buildSpec(EndpointRequest req, String apiKey) {
        RequestSpecification spec = client.baseSpec(env.getBaseUrl())
                .contentType(ContentType.URLENC)
                .formParam("token", req.getToken())
                .formParam("action", req.getAction());

        if (apiKey != null) {
            spec.header("X-Api-Key", apiKey);
            Attach.text("Заголовок X-Api-Key", apiKey);
        } else {
            Attach.text("Заголовок X-Api-Key", "не установлен (null)");
        }
        
        Attach.text("Параметры формы", 
            String.format("token: %s\naction: %s", req.getToken() != null ? req.getToken() : "null", req.getAction()));
        
        return spec;
    }
}
