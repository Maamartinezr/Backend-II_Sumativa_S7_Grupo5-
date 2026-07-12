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

    public static final String USUARIO_REQUEST = """
            {
              "username": "usuario1",
              "nombre": "Maria",
              "apellido": "Martinez",
              "email": "usuario1@minimarket.cl",
              "direccion": "Av. Principal 123",
              "password": "Password123*"
            }
            """;

    public static final String USUARIO_RESPONSE = """
            {
              "id": 1,
              "username": "usuario1",
              "nombre": "Maria",
              "apellido": "Martinez",
              "email": "usuario1@minimarket.cl",
              "direccion": "Av. Principal 123",
              "password": "Password123*"
            }
            """;

    public static final String USUARIO_COLLECTION = """
            {
              "_embedded": {
                "usuarioList": [
                  {
                    "id": 1,
                    "username": "usuario1",
                    "nombre": "Maria",
                    "apellido": "Martinez",
                    "email": "usuario1@minimarket.cl",
                    "direccion": "Av. Principal 123",
                    "password": "Password123*"
                  }
                ]
              },
              "_links": {
                "self": {
                  "href": "http://localhost:9090/api/usuarios"
                }
              }
            }
            """;

    public static final String INVENTARIO_REQUEST = """
            {
              "producto": {
                "id": 1
              },
              "cantidad": 5,
              "tipoMovimiento": "ENTRADA",
              "fechaMovimiento": "2026-07-06T04:00:00.000+00:00"
            }
            """;

    public static final String INVENTARIO_RESPONSE = """
            {
              "id": 1,
              "producto": {
                "id": 1
              },
              "cantidad": 5,
              "tipoMovimiento": "ENTRADA",
              "fechaMovimiento": "2026-07-06T04:00:00.000+00:00"
            }
            """;

    public static final String INVENTARIO_COLLECTION = """
            {
              "_embedded": {
                "inventarioList": [
                  {
                    "id": 1,
                    "producto": {
                      "id": 1
                    },
                    "cantidad": 5,
                    "tipoMovimiento": "ENTRADA",
                    "fechaMovimiento": "2026-07-06T04:00:00.000+00:00"
                  }
                ]
              },
              "_links": {
                "self": {
                  "href": "http://localhost:9090/api/inventario"
                }
              }
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

    public static final String ERROR_404_USUARIO = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 404,
              "error": "Not Found",
              "message": "Usuario no encontrado con id: 99",
              "path": "/api/usuarios/99"
            }
            """;

    public static final String ERROR_404_INVENTARIO = """
            {
              "timestamp": "2026-07-06T04:12:49.271Z",
              "status": 404,
              "error": "Not Found",
              "message": "Movimiento de inventario no encontrado con id: 99",
              "path": "/api/inventario/99"
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
