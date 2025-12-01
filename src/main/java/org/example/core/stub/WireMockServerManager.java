package org.example.core.stub;

import io.qameta.allure.Step;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.example.core.allure.Attach;

public class WireMockServerManager {
    private WireMockServer server;

    public WireMockServer server() {
        return server;
    }

    @Step("Запуск WireMock сервера на порту {port}")
    public void start(int port) {
        Attach.text("Конфигурация WireMock", String.format("Порт: %d", port));
        server = new WireMockServer(WireMockConfiguration.options().port(port));
        server.start();
        Attach.text("Статус WireMock", "Сервер успешно запущен на порту " + port);
    }

    @Step("Остановка WireMock сервера")
    public void stop() {
        if (server != null) {
            server.stop();
            Attach.text("Статус WireMock", "Сервер успешно остановлен");
        } else {
            Attach.text("Статус WireMock", "Сервер не был запущен");
        }
    }
}
