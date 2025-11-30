package org.example.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EndpointRequest {
    String token;
    String action;
}
