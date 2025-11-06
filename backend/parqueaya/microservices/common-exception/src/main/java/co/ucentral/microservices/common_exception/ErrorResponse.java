package co.ucentral.microservices.common_exception;

import lombok.Builder;

import java.util.Map;

@Builder
public record ErrorResponse(Map<String,String> error) {
}
