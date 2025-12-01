package org.example.api;

import io.qameta.allure.*;
import org.example.BaseTest;
import org.example.api.enums.Action;
import org.example.core.util.StringGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Безопасность API")
@Feature("Проверка доступа к endpoint")
@Story("Разрешение на доступ")
@Owner("Oleg")
public class SecurityHeaderTests extends BaseTest {
    private static String errorExpectedResult = readResourceFile("src/test/resources/error.json");
    private static String successExpectedResult = readResourceFile("src/test/resources/success.json");

    @ParameterizedTest(name = "Действие: {0}, X-Api-Key: {1}, Токен: {2}, Ожидаемый результат: {3}")
    @MethodSource("testData")
    @Description("Проверка доступа к endpoint")
    void shouldAcceptWithValidApiKey(Action action, String key, String token, String result) {
        var req = step.createRequest("Отправить " + action.getValue(), action, token);
        var resp = endpoint.call(req, key);
        assertThat(resp.getResult())
                .as("Проверка поля result")
                .isNotNull()
                .isEqualTo(result.contains("OK") ? "OK" : "ERROR");

        assertThat(resp.getMessage())
                .as("Проверка поля message")
                .isNotEmpty();

        assertThat(resp.getRaw().getStatusCode())
                .as("Проверка статус-кода HTTP")
                .isBetween(200, 401);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(Action.LOGIN, env.getApiKey(), StringGenerator.validToken(), successExpectedResult),
                Arguments.of(Action.ACTION, env.getApiKey(), StringGenerator.validToken(), successExpectedResult),
                Arguments.of(Action.LOGOUT, env.getApiKey(), StringGenerator.validToken(), successExpectedResult),
                Arguments.of(Action.LOGIN, StringGenerator.randomString(7), StringGenerator.invalidShortToken(), errorExpectedResult),
                Arguments.of(Action.LOGIN, null, StringGenerator.invalidLongToken(), errorExpectedResult),
                Arguments.of(Action.LOGIN, env.getApiKey(), StringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.LOGIN, StringGenerator.randomString(10), null, errorExpectedResult),
                Arguments.of(Action.ACTION, StringGenerator.randomString(9), StringGenerator.invalidLongToken(), errorExpectedResult),
                Arguments.of(Action.ACTION, null, StringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.ACTION, env.getApiKey(), null, errorExpectedResult),
                Arguments.of(Action.ACTION, StringGenerator.randomString(1), StringGenerator.validToken(), errorExpectedResult),
                Arguments.of(Action.ACTION, env.getApiKey(), StringGenerator.invalidShortToken(), errorExpectedResult),
                Arguments.of(Action.LOGOUT, null, null, errorExpectedResult),
                Arguments.of(Action.LOGOUT, StringGenerator.randomString(32), StringGenerator.invalidShortToken(), errorExpectedResult),
                Arguments.of(Action.LOGOUT, env.getApiKey(), StringGenerator.invalidLongToken(), errorExpectedResult),
                Arguments.of(Action.LOGOUT, StringGenerator.randomString(5), StringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.INVALID, env.getApiKey(), StringGenerator.validToken(), errorExpectedResult),
                Arguments.of(Action.INVALID, null, StringGenerator.invalidTokenWithSymbols(), errorExpectedResult),
                Arguments.of(Action.NULL, env.getApiKey(), StringGenerator.validToken(), errorExpectedResult),
                Arguments.of(Action.NULL, StringGenerator.randomString(5), StringGenerator.validToken(), errorExpectedResult)
        );
    }
}
