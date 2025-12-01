package org.example.api;

import io.qameta.allure.*;
import org.example.BaseTest;
import org.example.api.enums.Action;
import org.example.core.listener.TestListener;
import org.example.core.util.StringGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


@ExtendWith(TestListener.class)
@Epic("API Тестирование")
@Feature("Проверка безопасности заголовков")
public class SecurityHeaderTests extends BaseTest {
    private static String errorExpectedResult = readResourceFile("src/test/resources/error.json");
    private static String successExpectedResult = readResourceFile("src/test/resources/success.json");

    @ParameterizedTest()
    @MethodSource("testData")
    @Description("Проверка доступа к endpoint с вaлидными данными")
    @Story("Валидация безопасности API")
    @Feature("Positive tests")
    @Severity(SeverityLevel.CRITICAL)
    void checkEndpoint(Action action, String key, String token, String result) {
        var req = step.createRequest("Отправить " + action.getValue(), action, token);
        var resp = endpoint.call(req, key);
        step.validateResponse(resp, result);
    }

    @ParameterizedTest()
    @MethodSource("negativeData")
    @Description("Проверка доступа к endpoint с невалидными данными")
    @Story("Валидация безопасности API")
    @Feature("Negative tests")
    @Severity(SeverityLevel.CRITICAL)
    void checkEndpointNegative(Action action, String key, String token, String result) {
        var req = step.createRequest("Отправить " + action.getValue(), action, token);
        var resp = endpoint.call(req, key);
        step.validateResponse(resp, result);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(Action.LOGIN, env.getApiKey(), stringGenerator.validToken(), successExpectedResult),
                Arguments.of(Action.ACTION, env.getApiKey(), stringGenerator.validToken(), successExpectedResult),
                Arguments.of(Action.LOGOUT, env.getApiKey(), stringGenerator.validToken(), successExpectedResult)
        );
    }

    private static Stream<Arguments> negativeData() {
        return Stream.of(
                Arguments.of(Action.LOGIN, StringGenerator.randomString(7), stringGenerator.invalidShortToken(), errorExpectedResult),
                Arguments.of(Action.LOGIN, null, stringGenerator.invalidLongToken(), errorExpectedResult),
                Arguments.of(Action.LOGIN, env.getApiKey(), stringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.LOGIN, StringGenerator.randomString(10), null, errorExpectedResult),
                Arguments.of(Action.ACTION, StringGenerator.randomString(9), stringGenerator.invalidLongToken(), errorExpectedResult),
                Arguments.of(Action.ACTION, null, stringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.ACTION, env.getApiKey(), null, errorExpectedResult),
                Arguments.of(Action.ACTION, StringGenerator.randomString(1), stringGenerator.validToken(), errorExpectedResult),
                Arguments.of(Action.ACTION, env.getApiKey(), stringGenerator.invalidShortToken(), errorExpectedResult),
                Arguments.of(Action.LOGOUT, null, null, errorExpectedResult),
                Arguments.of(Action.LOGOUT, StringGenerator.randomString(32), stringGenerator.invalidShortToken(), errorExpectedResult),
                Arguments.of(Action.LOGOUT, env.getApiKey(), stringGenerator.invalidLongToken(), errorExpectedResult),
                Arguments.of(Action.LOGOUT, StringGenerator.randomString(5), stringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.INVALID, env.getApiKey(), stringGenerator.validToken(), errorExpectedResult),
                Arguments.of(Action.INVALID, null, stringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.NULL, env.getApiKey(), stringGenerator.validToken(), errorExpectedResult),
                Arguments.of(Action.NULL, StringGenerator.randomString(5), stringGenerator.validToken(), errorExpectedResult)
        );
    }
}
