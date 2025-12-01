package org.example.core.config;

import io.qameta.allure.Step;
import lombok.Getter;
import org.example.core.allure.Attach;

import java.io.InputStream;
import java.util.Properties;

@Getter
public class TestEnv {
    private final String baseUrl;
    private final String apiKey;
    private final String mockUrl;
    private final int wiremockPort;
    private final int httpConnectionTimeout;
    private final int httpSocketTimeout;
    private final boolean relaxedHttpsValidation;
    private final int tokenValidLength;

    public TestEnv() {
        Properties props = loadProperties();
        
        this.baseUrl = props.getProperty("app.base.url", "http://localhost:8080");
        this.apiKey = props.getProperty("app.api.key", "qazWSXedc");
        this.mockUrl = props.getProperty("wiremock.url", "http://localhost:8888");
        this.wiremockPort = Integer.parseInt(props.getProperty("wiremock.port", "8888"));
        this.httpConnectionTimeout = Integer.parseInt(props.getProperty("http.timeout.connection", "5000"));
        this.httpSocketTimeout = Integer.parseInt(props.getProperty("http.timeout.socket", "10000"));
        this.relaxedHttpsValidation = Boolean.parseBoolean(props.getProperty("http.relaxed.https.validation", "true"));
        this.tokenValidLength = Integer.parseInt(props.getProperty("token.valid.length", "32"));
    }

    @Step("Загрузка конфигурации из application.properties")
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                Attach.text("Предупреждение", "Файл application.properties не найден, используются значения по умолчанию");
                return props;
            }
            props.load(input);
            Attach.text("Конфигурация загружена", "Файл application.properties успешно прочитан");
        } catch (Exception e) {
            Attach.text("Ошибка загрузки конфигурации", 
                String.format("Не удалось загрузить application.properties: %s\nИспользуются значения по умолчанию", e.getMessage()));
        }
        return props;
    }
}
