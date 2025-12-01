package org.example;

import io.qameta.allure.Step;
import org.example.api.EndpointApi;
import org.example.core.allure.Attach;
import org.example.core.allure.Steps;
import org.example.core.config.TestEnv;
import org.example.core.http.RestAssuredClient;
import org.example.core.stub.WireMockServerManager;
import org.example.service.ExternalServiceMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Files;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected static TestEnv env;
    protected static Steps step;
    protected static RestAssuredClient restAssuredClient;
    protected static EndpointApi endpoint;
    protected static org.example.core.util.StringGenerator stringGenerator;
    protected WireMockServerManager wireMockServerManager;

    @BeforeAll
    @Step("Инициализация тестового окружения")
    void beforeAll() {
        Attach.text("Этап", "Инициализация перед всеми тестами");
        
        env = new TestEnv();
        Attach.text("Конфигурация окружения", 
            String.format("Base URL: %s\nAPI Key: %s\nMock URL: %s", 
                env.getBaseUrl(), env.getApiKey(), env.getMockUrl()));
        
        restAssuredClient = new RestAssuredClient(env);
        endpoint = new EndpointApi(env, restAssuredClient);
        
        wireMockServerManager = new WireMockServerManager();
        wireMockServerManager.start(env.getWiremockPort());
        
        step = new Steps();
        stringGenerator = new org.example.core.util.StringGenerator(env);
        
        Attach.text("Статус инициализации", "Все компоненты успешно инициализированы");
    }

    @BeforeEach
    @Step("Подготовка заглушек перед каждым тестом")
    void resetStubs() {
        Attach.text("Этап", "Подготовка перед каждым тестом");
        ExternalServiceMock.reset(wireMockServerManager.server());
        ExternalServiceMock.stubAuthOk(wireMockServerManager.server());
        ExternalServiceMock.stubDoActionOk(wireMockServerManager.server());
        Attach.text("Статус подготовки", "Все заглушки настроены и готовы к использованию");
    }

    @AfterAll
    @Step("Завершение тестового окружения")
    void afterAll() {
        Attach.text("Этап", "Завершение после всех тестов");
        wireMockServerManager.stop();
        Attach.text("Статус завершения", "Все ресурсы освобождены");
    }

    @Step("Чтение файла ресурсов: {path}")
    protected static String readResourceFile(String path) {
        try {
            String content = Files.readString(Paths.get(path));
            Attach.text("Содержимое файла " + path, content);
            return content;
        } catch (Exception e) {
            Attach.text("Ошибка чтения файла", 
                String.format("Путь: %s\nОшибка: %s", path, e.getMessage()));
            throw new RuntimeException("Ошибка чтения файла: " + path, e);
        }
    }
}
