package org.example.core.step;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.api.dto.EndpointRequest;
import org.example.api.dto.EndpointResponse;
import org.example.core.allure.Attach;
import org.example.core.config.TestEnv;
import org.example.core.http.RestAssuredClient;

public class EndpointStep {

    @Step("ℹ Отправляем запрос в эндпоинт сервиса с action: {request.action}")
    public static Response sendRequest(EndpointRequest request, String apiKey, TestEnv env, RestAssuredClient client) {
        RequestSpecification spec = client.baseSpec(env.getBaseUrl())
                .contentType(ContentType.URLENC)
                .formParam("token", request.getToken())
                .formParam("action", request.getAction());

        if (apiKey != null) {
            spec.header("X-Api-Key", apiKey);
        }

        Attach.text("Request spec", spec.toString());
        return spec.post("/endpoint").then().extract().response();
    }

    @Step("📥 Получаем ответ от сервиса")
    public static EndpointResponse parseResponse(Response response) {
        String payload = response.asString();
        String result = payload.contains("\"OK\"") ? "OK" : "ERROR";
        String message = payload.contains("message") ? response.jsonPath().getString("message") : null;
        Attach.text("Статус-код", String.valueOf(response.getStatusCode()));
        Attach.text("Тело ответа", payload);
        return new EndpointResponse(result, message, response);
    }
}
