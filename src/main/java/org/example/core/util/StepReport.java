package org.example.core.util;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.dto.EndpointRequest;
import org.example.api.dto.EndpointResponse;
import org.example.core.allure.Attach;

public class StepReport {

    @Step("{action}")
    public static void report(String action,
                              EndpointRequest request,
                              Response rawResponse,
                              EndpointResponse parsedResponse,
                              String expectedJson) {

        Attach.text("Отправленный запрос:\n", request.toString());
        Attach.text("Статус-код ответа: ", String.valueOf(rawResponse.getStatusCode()));
        Attach.text("Тело ответа:\n", rawResponse.asString());
        Attach.text("Ожидаемый результат:\n", expectedJson);
        Attach.text("Фактический результат:\n", parsedResponse.toString());
    }
}
