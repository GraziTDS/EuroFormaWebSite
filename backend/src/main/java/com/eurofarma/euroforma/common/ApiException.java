package com.eurofarma.euroforma.common;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static ApiException naoEncontrado(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, message);
    }

    public static ApiException conflito(String message) {
        return new ApiException(HttpStatus.CONFLICT, message);
    }

    public static ApiException requisicaoInvalida(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }

    public static ApiException naoAutorizado(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, message);
    }

    public static ApiException proibido(String message) {
        return new ApiException(HttpStatus.FORBIDDEN, message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
