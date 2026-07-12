package com.minimarket.util;

public final class ApiErrorMessages {

    private ApiErrorMessages() {
    }

    public static final String VALIDATION_INVALID_DATA = "La solicitud contiene datos invalidos";
    public static final String DATA_CONFLICT = "Conflicto de datos: el recurso ya existe o viola restricciones";
    public static final String UNEXPECTED_ERROR = "Ha ocurrido un error inesperado";
}
