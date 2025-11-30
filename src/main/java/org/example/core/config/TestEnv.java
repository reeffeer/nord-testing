package org.example.core.config;

import lombok.Getter;

@Getter
public class TestEnv {
    private final String baseUrl = "http://localhost:8080";
    private final String apiKey = "qazWSXedc";
    private final String mockUrl = "http://localhost:8888";
}
