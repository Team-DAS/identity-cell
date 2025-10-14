# Authz Service

Microservicio de autorización para el proyecto UdeAJobs, responsable de validar tokens JWT y gestionar la autorización de usuarios dentro de la arquitectura de Identity Cell.

## Descripción

El **Authz Service** es un microservicio desarrollado en Spring Boot que proporciona funcionalidades de validación de tokens JWT. Su propósito principal es actuar como un servicio centralizado de autorización dentro de una arquitectura de microservicios, permitiendo que otros servicios validen la autenticidad y validez de los tokens de acceso de los usuarios.

Este servicio forma parte de la **Identity Cell**, una célula de microservicios dedicada a la gestión de identidad y autenticación/autorización en el ecosistema UdeAJobs.

## Tecnologías

- **Java 21**: Lenguaje de programación principal
- **Spring Boot 3.5.5**: Framework de aplicación
- **Spring Web**: Para la creación de APIs RESTful
- **JJWT 0.13.0**: Librería para el manejo y validación de JSON Web Tokens
- **Lombok**: Para reducir código boilerplate
- **Spring Boot Actuator**: Para monitoreo y métricas
- **Micrometer Prometheus**: Para exportación de métricas
- **Gradle**: Herramienta de construcción

## Arquitectura

### Estructura de Capas

El servicio sigue una arquitectura en capas bien definida:

```
├── Controller Layer (API REST)
│   └── ValidateController
├── Service Layer (Lógica de negocio)
│   └── ValidateService
├── Util Layer (Utilidades)
│   └── JwtValidator
├── Exception Layer (Manejo de errores)
│   ├── GlobalExceptionHandler
│   ├── InvalidTokenException
│   └── TokenIsNotPresentException
└── DTO Layer (Objetos de transferencia)
    └── ErrorResponse
```

### Componentes Principales

#### 1. ValidateController
**Ruta base**: `/api/v1/authz`

Controlador REST que expone el endpoint de validación de tokens.

**Endpoint**:
- `POST /api/v1/authz/validate`: Valida un token JWT recibido en el header Authorization

#### 2. ValidateService
Servicio que contiene la lógica de negocio para la validación de tokens. Se encarga de:
- Extraer el token del header Authorization
- Verificar el formato Bearer
- Delegar la validación criptográfica al JwtValidator
- Lanzar excepciones apropiadas en caso de errores

#### 3. JwtValidator
Utilidad que maneja la validación criptográfica de tokens JWT utilizando la librería JJWT. Funcionalidades:
- Validación de firma del token usando HMAC-SHA
- Extracción de claims del token
- Verificación de expiración y estructura del token

#### 4. GlobalExceptionHandler
Manejador global de excepciones que intercepta errores específicos y los convierte en respuestas HTTP apropiadas:
- `InvalidTokenException` → HTTP 401 (Unauthorized)
- `TokenIsNotPresentException` → HTTP 400 (Bad Request)

## API REST

### Validar Token

Valida la autenticidad y validez de un token JWT.

**Request**:
```
POST /api/v1/authz/validate
Headers:
  Authorization: Bearer <token_jwt>
```

**Responses**:

- **200 OK**: Token válido
  ```json
  true
  ```

- **401 Unauthorized**: Token inválido o expirado
  ```json
  {
    "timestamp": "2025-10-14T10:30:00",
    "Status": 401,
    "error": "Unauthorized",
    "message": "Invalid Token"
  }
  ```

- **400 Bad Request**: Token no presente o formato incorrecto
  ```json
  {
    "timestamp": "2025-10-14T10:30:00",
    "Status": 400,
    "error": "Bad Request",
    "message": "Token is not present"
  }
  ```

## Configuración

### Variables de Entorno

#### Perfil de Desarrollo (`dev`)
- `server.port`: 8083
- `jwt.secret`: Clave secreta hardcodeada para desarrollo

#### Perfil de Producción (`prod`)
- `SERVER_PORT`: Puerto del servidor (default: 8082)
- `JWT_SECRET`: Clave secreta para validación de tokens (obligatorio)

### Propiedades de Aplicación

**Comunes**:
- `spring.application.name`: authz-service
- `server.error.include-stacktrace`: never (seguridad)

**Producción**:
- `management.endpoints.web.exposure.include`: prometheus,health,info (métricas y monitoreo)

## Seguridad

### Validación de Tokens

El servicio utiliza HMAC-SHA con una clave secreta codificada en Base64 para validar la firma de los tokens JWT. El proceso de validación incluye:

1. Verificación del formato Bearer en el header Authorization
2. Extracción del token (elimina prefijo "Bearer ")
3. Validación de la firma criptográfica
4. Verificación de la expiración del token
5. Validación de la estructura del token

### Consideraciones de Seguridad

- La clave secreta JWT debe mantenerse confidencial
- En producción, la clave se configura mediante variable de entorno
- Los stack traces están deshabilitados en las respuestas de error
- Los tokens deben enviarse siempre mediante HTTPS en producción

## Monitoreo y Métricas

El servicio incluye Spring Boot Actuator con exportación de métricas a Prometheus, permitiendo:

- **Health checks**: Verificación del estado del servicio
- **Métricas de Prometheus**: Monitoreo de rendimiento y uso
- **Info endpoint**: Información sobre la aplicación

Endpoints disponibles en producción:
- `/actuator/health`
- `/actuator/info`
- `/actuator/prometheus`

## Containerización

El servicio incluye un Dockerfile para su despliegue en contenedores:

- **Imagen base**: eclipse-temurin:21-jdk-jammy
- **Perfil activo**: prod (por defecto en contenedor)
- **Puerto expuesto**: Configurado mediante variable de entorno

## Integración en Microservicios

Este servicio está diseñado para ser utilizado por otros microservicios dentro de la arquitectura UdeAJobs. Los servicios clientes pueden realizar llamadas HTTP al endpoint de validación para verificar tokens de usuario antes de procesar solicitudes.

### Flujo de Autorización

1. Un usuario realiza una solicitud a un microservicio con su token JWT
2. El microservicio receptor extrae el token y lo envía al Authz Service
3. Authz Service valida el token y retorna el resultado
4. El microservicio receptor procede o rechaza la solicitud basándose en la respuesta

## Manejo de Errores

El servicio implementa un manejo de errores robusto con respuestas estructuradas:

### ErrorResponse DTO
```java
{
  "timestamp": LocalDateTime,
  "Status": int,
  "error": String,
  "message": String
}
```

### Excepciones Personalizadas

- **InvalidTokenException**: Token JWT inválido, expirado o con firma incorrecta
- **TokenIsNotPresentException**: Header Authorization ausente o con formato incorrecto

## Versión

- **Versión actual**: 0.0.1-SNAPSHOT
- **API Version**: v1

---

**Proyecto**: UdeAJobs  
**Célula**: Identity Cell  
**Servicio**: Authorization Service
