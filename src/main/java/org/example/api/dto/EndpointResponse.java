package org.example.api.dto;

import io.restassured.response.Response;
import lombok.Value;

@Value
public class EndpointResponse {
    String result;
    String message;
    Response raw;

    @Override
    public String toString() {
        if (message == null) {
            return String.format("{\n  \"result\": \"%s\"\n}", result);
        } else {
            return String.format("{\n  \"result\": \"%s\",\n  \"message\": \"%s\"\n}", result, message);
        }
    }
}
