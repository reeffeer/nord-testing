package org.example.core.allure;

import io.qameta.allure.Step;
import org.example.api.dto.EndpointRequest;
import org.example.api.enums.Action;

public class Steps {
    @Step("{message}")
    public static EndpointRequest createRequest(String message, Action action, String token) {
        return EndpointRequest.builder()
                .token(token)
                .action(action.getValue())
                .build();
    }
}
