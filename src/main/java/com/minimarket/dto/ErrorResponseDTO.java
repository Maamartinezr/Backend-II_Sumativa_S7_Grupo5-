package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(name = "ErrorResponse")
public class ErrorResponseDTO {

    @Schema(example = "2026-07-06T04:12:49.271Z")
    private Instant timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "La solicitud contiene datos invalidos")
    private String message;

    @Schema(example = "/api/productos")
    private String path;

    private Map<String, String> validationErrors;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
