# Minimarket Backend - Backend II Sumativa S7

Proyecto backend desarrollado con **Java** y **Spring Boot** para la gestión de un minimarket.  
Incluye módulos de productos, categorías, inventario, ventas, detalle de ventas, usuarios, seguridad y documentación de API.

---

## Descripción del proyecto

Este sistema corresponde a un backend REST orientado a la administración de un minimarket.  
Su objetivo es exponer servicios para la gestión de entidades principales del negocio, con arquitectura por capas y buenas prácticas de mantenibilidad:

- **Controller**
- **Service**
- **Repository**
- **Entity**
- **DTO**
- **Security**
- **Exception Handling**
- **Test**

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Security**
- **Spring Data JPA**
- **Bean Validation**
- **springdoc-openapi (Swagger UI)**
- **JUnit 5**
- **Mockito**
- **Maven**
- **Jenkins**

---

## Características principales

- API REST para administración de minimarket
- CRUD de entidades principales
- Seguridad con autenticación JWT
- Autorización por roles en endpoints críticos
- Documentación interactiva con Swagger UI (OpenAPI 3)
- Contratos de entrada/salida mediante DTOs
- Manejo global de excepciones
- Pruebas unitarias y de capa controller
- Estructura preparada para integración continua con Jenkins

---

## Estructura general del proyecto

```bash
MINIMARKET_S6/
│
├── src/
│   ├── main/
│   │   ├── java/com/minimarket/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   │   ├── config/
│   │   │   │   ├── filter/
│   │   │   │   ├── model/
│   │   │   │   ├── service/
│   │   │   │   └── util/
│   │   │   ├── service/
│   │   │   ├── exception/
│   │   │   └── MinimarketApplication.java
│   │
│   └── resources/
│       └── application.properties
│
├── src/test/java/com/minimarket/
├── pom.xml
└── README.md
```

> Nota: la estructura exacta puede variar según la rama, pero esta representa la organización objetivo actual del proyecto.

---

## Módulos funcionales

- **Productos**
- **Categorías**
- **Inventario**
- **Ventas**
- **Detalle de ventas**
- **Usuarios**
- **Carrito**
- **Autenticación/Autorización**

---

## Seguridad (JWT)

La API utiliza seguridad con Spring Security y autenticación por token JWT.

### Comportamiento principal

- Acceso público a:
  - `/public/**`
  - `/api/auth/**`
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
  - `/swagger-ui.html`
- Endpoints protegidos por autenticación
- Reglas por rol para operaciones críticas (por ejemplo, creación/edición/eliminación en módulos sensibles)

---

## Autenticación

### Endpoint de login

```http
POST /api/auth/login
Content-Type: application/json
```

### Body de ejemplo

```json
{
  "username": "admin",
  "password": "Admin123*"
}
```

### Respuesta esperada (ejemplo)

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "tokenType": "Bearer"
}
```

### Uso del token

Incluir el token en el header:

```http
Authorization: Bearer <tu_token_jwt>
```

---

## Documentación de API (OpenAPI / Swagger UI)

Este proyecto usa **springdoc-openapi** para generar documentación automática e interactiva.

### URLs

- Swagger UI: `http://localhost:9090/swagger-ui.html`
- Alternativa: `http://localhost:9090/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:9090/v3/api-docs`

### Qué puedes validar en Swagger

- Endpoints por módulo
- Parámetros de ruta/query
- Esquemas de request/response
- Códigos HTTP esperados
- Pruebas interactivas con **Try it out**
- Autenticación JWT mediante **Authorize**

---

## Validación con Postman

1. Levanta la aplicación.
2. Importa en Postman: `http://localhost:9090/v3/api-docs`.
3. Ejecuta `POST /api/auth/login` para obtener token.
4. Configura autorización tipo **Bearer Token**.
5. Prueba endpoints protegidos y valida:
   - `200 OK`
   - `201 Created`
   - `204 No Content`
   - `400 Bad Request`
   - `401 Unauthorized`
   - `403 Forbidden`
   - `404 Not Found`

---

## Endpoints principales (referenciales)

### Auth
- `POST /api/auth/login`

### Productos
- `GET /api/productos`
- `GET /api/productos/{id}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`

### Carrito
- `GET /api/carrito`
- `GET /api/carrito/{id}`
- `POST /api/carrito`
- `PUT /api/carrito/{id}`
- `DELETE /api/carrito/{id}`

### Categorías
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `POST /api/categorias`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}`

### Inventario
- `GET /api/inventario`
- `GET /api/inventario/{id}`
- `POST /api/inventario`
- `PUT /api/inventario/{id}`
- `DELETE /api/inventario/{id}`

### Ventas
- `GET /api/ventas`
- `GET /api/ventas/{id}`
- `POST /api/ventas`

### Detalle de ventas
- `GET /api/detalle-ventas`
- `GET /api/detalle-ventas/{id}`
- `POST /api/detalle-ventas`
- `PUT /api/detalle-ventas/{id}`
- `DELETE /api/detalle-ventas/{id}`

### Usuarios
- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

### Público
- `GET /public/hola`

---

## Requisitos previos

- **Java 17**
- **Maven**
- **Git**
- IDE (VS Code / IntelliJ / Eclipse)

---

## Instalación y ejecución local

### 1) Clonar repositorio

```bash
git clone https://github.com/Maamartinezr/Backend-II_Sumativa_S6_Grupo5.git
cd Backend-II_Sumativa_S6_Grupo5
```

### 2) Compilar

```bash
.\mvnw.cmd clean install
```

### 3) Ejecutar

```bash
.\mvnw.cmd spring-boot:run
```

> La app corre en `http://localhost:9090`.

---

## Ejecución de pruebas

```bash
.\mvnw.cmd verify
```

o

```bash
.\mvnw.cmd clean test
```

---

## Integración continua (Jenkins)

Flujo sugerido de pipeline:

1. Checkout
2. `.\mvnw.cmd clean install`
3. `.\mvnw.cmd clean test`
4. Publicación de resultados

---

## Estado actual y mejoras implementadas

En la evolución reciente del proyecto se incorporaron/mejoraron:

- Documentación OpenAPI con Swagger UI
- Flujo de autenticación JWT
- Endpoints de autenticación
- Uso de DTOs para desacoplar entidad de contratos API
- Validaciones con Bean Validation
- Mejoras de consistencia de respuestas HTTP
- Fortalecimiento de pruebas en capa controller/servicio

---

## Próximas mejoras sugeridas

- Cobertura de pruebas más amplia (incluyendo seguridad y casos límite)
- Mayor estandarización de respuestas de error
- Versionado de API
- Hardening adicional de seguridad
- Pipeline Jenkins con despliegue automatizado
- Documentación técnica de base de datos y arquitectura

---

## Autor

Proyecto desarrollado por **Maamartinezr**.

---

## Licencia

Este proyecto no incluye licencia definida actualmente.  
Se recomienda agregar una licencia como MIT, Apache 2.0 o GPL-3.0 según el objetivo del repositorio.
