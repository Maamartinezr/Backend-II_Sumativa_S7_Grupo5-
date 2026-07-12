package com.minimarket.util;

public final class OpenApiExamples {

    private OpenApiExamples() {
    }

    public static final String PRODUCTO_REQUEST = """
            {
              "nombre": "Arroz integral",
              "precio": 1500.0,
              "stock": 20,
              "categoriaId": 1
            }
            """;

    public static final String PRODUCTO_RESPONSE = """
            {
              "id": 1,
              "nombre": "Arroz integral",
              "precio": 1500.0,
              "stock": 20,
              "categoriaId": 1
            }
            """;

    public static final String PRODUCTO_PAGE = """
            {
              "content": [
                {
                  "id": 1,
                  "nombre": "Arroz integral",
                  "precio": 1500.0,
                  "stock": 20,
                  "categoriaId": 1
                }
              ],
              "number": 0,
              "size": 20,
              "totalElements": 1,
              "totalPages": 1,
              "first": true,
              "last": true,
              "empty": false
            }
            """;

    public static final String CARRITO_REQUEST = """
            {
              "usuarioId": 1,
              "productoId": 1,
              "cantidad": 2
            }
            """;

    public static final String CARRITO_RESPONSE = """
            {
              "id": 1,
              "usuarioId": 1,
              "productoId": 1,
              "cantidad": 2
            }
            """;

    public static final String CARRITO_PAGE = """
            {
              "content": [
                {
                  "id": 1,
                  "usuarioId": 1,
                  "productoId": 1,
                  "cantidad": 2
                }
              ],
              "number": 0,
              "size": 20,
              "totalElements": 1,
              "totalPages": 1,
              "first": true,
              "last": true,
              "empty": false
            }
            """;

    public static final String ERROR_400 = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 400,
              "error": "Bad Request",
              "message": "La solicitud contiene datos invalidos",
              "path": "/api/productos"
            }
            """;

    public static final String ERROR_401 = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 401,
              "error": "Unauthorized",
              "message": "Full authentication is required to access this resource",
              "path": "/api/productos"
            }
            """;

    public static final String ERROR_403 = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 403,
              "error": "Forbidden",
              "message": "Access Denied",
              "path": "/api/productos"
            }
            """;

    public static final String ERROR_404_PRODUCTO = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 404,
              "error": "Not Found",
              "message": "Producto no encontrado con id: 99",
              "path": "/api/productos/99"
            }
            """;

    public static final String ERROR_404_CARRITO = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 404,
              "error": "Not Found",
              "message": "Carrito no encontrado con id: 99",
              "path": "/api/carrito/99"
            }
            """;

    public static final String ERROR_409 = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 409,
              "error": "Conflict",
              "message": "Conflicto de datos: el recurso ya existe o viola restricciones",
              "path": "/api/productos"
            }
            """;

    public static final String ERROR_500 = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 500,
              "error": "Internal Server Error",
              "message": "Ha ocurrido un error inesperado",
              "path": "/api/productos"
            }
            """;
}
