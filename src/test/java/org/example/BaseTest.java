package org.example;

import org.example.api.EndpointApi;
import org.example.core.allure.Attach;
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
    protected TestEnv env;
    protected RestAssuredClient restAssuredClient;
    protected EndpointApi endpoint;
    protected WireMockServerManager wireMockServerManager;

    @BeforeAll
    void beforeAll() {
        env = new TestEnv();
        restAssuredClient = new RestAssuredClient();
        endpoint = new EndpointApi(env, restAssuredClient);
        wireMockServerManager = new WireMockServerManager();
        wireMockServerManager.start(8888);
    }

    @BeforeEach
    void resetStubs() {
        ExternalServiceMock.reset(wireMockServerManager.server());
        ExternalServiceMock.stubAuthOk(wireMockServerManager.server());
        ExternalServiceMock.stubDoActionOk(wireMockServerManager.server());
    }

    @AfterAll
    void afterAll() {
        wireMockServerManager.stop();
    }

    protected String readResourceFile(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (Exception e) {
            Attach.text("[ERROR]Ошибка чтения файла: {path}\n", e.getMessage());
            throw new RuntimeException("Ошибка чтения файла: " + path, e);
        }
    }
}
