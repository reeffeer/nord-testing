package org.example.service;

import io.qameta.allure.Step;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.example.core.allure.Attach;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ExternalServiceMock {

    @Step("Сброс всех заглушек WireMock")
    public static void reset(WireMockServer wmServer) {
        wmServer.resetAll();
        Attach.text("Действие", "Все заглушки WireMock были сброшены");
    }

    @Step("Настройка заглушки для успешной аутентификации (POST /auth -> 200)")
    public static void stubAuthOk(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/auth"))
            .willReturn(aResponse().withStatus(200).withBody("{}")));
        Attach.text("Заглушка", "POST /auth -> HTTP 200 OK");
    }

    @Step("Настройка заглушки для неуспешной аутентификации (POST /auth -> 401)")
    public static void stubAuthFail(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/auth"))
            .willReturn(aResponse().withStatus(401).withBody("{}")));
        Attach.text("Заглушка", "POST /auth -> HTTP 401 Unauthorized");
    }

    @Step("Настройка заглушки для успешного выполнения действия (POST /doAction -> 200)")
    public static void stubDoActionOk(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(200).withBody("{}")));
        Attach.text("Заглушка", "POST /doAction -> HTTP 200 OK");
    }

    @Step("Настройка заглушки для неуспешного выполнения действия (POST /doAction -> 401)")
    public static void stubDoActionFail(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(401).withBody("{}")));
        Attach.text("Заглушка", "POST /doAction -> HTTP 401 Unauthorized");
    }
}
