package org.example.api;

import io.qameta.allure.*;
import org.example.BaseTest;
import org.example.api.dto.EndpointRequest;
import org.example.api.enums.Action;
import org.example.core.allure.Attach;
import org.example.core.allure.Steps;
import org.example.core.util.StepReport;
import org.example.core.util.TokenGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Безопасность API")
@Feature("Проверка заголовков безопасности")
@Story("Доступ к эндпоинту")
@Owner("Oleg")
public class SecurityHeaderTests extends BaseTest {
    private String errorExpectedResult = readResourceFile("src/test/resources/error.json");
    private String successExpectedResult = readResourceFile("src/test/resources/success.json");

    @Test
    @Story("Проверка в доступе")
    @Description("Проверка отказа в доступе без X-Api-Key")
    void shouldRejectWithoutApiKey() {

        var req = EndpointRequest.builder()
                .token(TokenGenerator.validToken())
                .action(Action.LOGIN.getValue()).build();

        var resp = endpoint.call(req, null);
        StepReport.report("Отправить " + Action.LOGIN.getValue() + " без X-Api-Key",
                req, resp.getRaw(), resp, errorExpectedResult);
//        Attach.json("Ожидаемый ответ:\n", errorExpectedResult);
//        Attach.json("Фактический ответ:\n", resp.toString());
        assertEquals("ERROR", resp.getResult(), "Без ключа доступ запрещен");
    }

    @Test
    @Story("Разрешение на доступ")
    @Description("Проверка доступа с валидным X-Api-Key")
    void shouldAcceptWithValidApiKey() {
        var req = EndpointRequest.builder()
                .token(TokenGenerator.validToken())
                .action("LOGIN").build();
        var resp = endpoint.call(req, env.getApiKey());
        Attach.json("Ожидаемый ответ:\n", successExpectedResult);
        Attach.json("Фактический ответ:\n", resp.toString());
        assertNotNull(resp.getResult(), "С валидным ключом сервис отвечает предсказуемо");
    }
}
