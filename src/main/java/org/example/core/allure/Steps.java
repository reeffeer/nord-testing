package org.example.core.allure;

import io.qameta.allure.Step;
import org.example.api.dto.EndpointRequest;
import org.example.api.enums.Action;

import static org.assertj.core.api.Assertions.assertThat;

public class Steps {
    
    @Step("Создание запроса: действие = {action}, токен = {token}")
    public EndpointRequest createRequest(String message, Action action, String token) {
        Attach.text("Параметры запроса", 
            String.format("Действие: %s\nТокен: %s", action.getValue(), token != null ? token : "null"));
        
        EndpointRequest request = EndpointRequest.builder()
                .token(token)
                .action(action.getValue())
                .build();
        
        Attach.json("Созданный запрос", 
            String.format("{\n  \"action\": \"%s\",\n  \"token\": \"%s\"\n}", 
                request.getAction(), request.getToken() != null ? request.getToken() : "null"));
        return request;
    }

    @Step("Валидация ответа: проверка полей result, message и HTTP статус-кода")
    public void validateResponse(org.example.api.dto.EndpointResponse resp, String expectedResult) {
        String expectedStatus = expectedResult.contains("OK") ? "OK" : "ERROR";

        Attach.text("Ожидаемый результат", expectedStatus);
        Attach.text("Фактический результат", resp.getResult());

        assertThat(resp.getResult())
                .as("Проверка поля result")
                .isNotNull()
                .isEqualTo(expectedStatus);

        Attach.text("Проверка message", resp.getMessage() != null ? resp.getMessage() : "null");
        assertThat(resp.getMessage())
                .as("Проверка поля message")
                .isNotEmpty();

        int statusCode = resp.getRaw().getStatusCode();
        Attach.text("HTTP статус-код", String.valueOf(statusCode));
        assertThat(statusCode)
                .as("Проверка статус-кода HTTP")
                .isBetween(200, 401);

        Attach.text("Результат валидации", "Все проверки пройдены успешно");
    }
}
