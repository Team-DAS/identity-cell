# Auth Service - UdeAJobs Identity Cell

## Descripción General

**Auth Service** es un microservicio de autenticación desarrollado con Spring Boot que forma parte de la celda de identidad (Identity Cell) del ecosistema UdeAJobs. Este servicio se encarga de gestionar la autenticación de usuarios, la generación y validación de tokens JWT, y el manejo de tokens de refresco para mantener sesiones seguras.

## Arquitectura del Servicio

### Stack Tecnológico

- **Framework**: Spring Boot 3.5.5
- **Lenguaje**: Java 21
- **Base de Datos**: MongoDB
- **Seguridad**: Spring Security + OAuth2 Client
- **Autenticación**: JWT (JSON Web Tokens) usando JJWT 0.13.0
- **Monitoreo**: Spring Boot Actuator + Prometheus
- **Build Tool**: Gradle

### Componentes Principales

#### 1. Capa de Entidades (`entity/`)

**User**
- Entidad principal que representa a un usuario en el sistema
- Implementa `UserDetails` de Spring Security para integración con el framework
- Almacenada en la colección `user` de MongoDB
- Campos principales:
  - `id`: Identificador único del usuario
  - `username`: Nombre de usuario
  - `password`: Contraseña encriptada
  - `email`: Correo electrónico (usado como username en la autenticación)
  - `role`: Rol del usuario para autorización

**RefreshToken**
- Entidad para gestionar tokens de refresco
- Permite renovar access tokens sin requerir credenciales nuevamente
- Almacena información sobre la validez y expiración del token

#### 2. Capa de Controladores (`controller/`)

**AuthController** (`/api/v1/auth`)
- **POST** `/login`: Endpoint para autenticación de usuarios
  - Recibe credenciales (email y contraseña)
  - Retorna un access token y refresh token
- **POST** `/refresh-token`: Endpoint para renovar access tokens
  - Recibe un refresh token válido
  - Retorna un nuevo access token

#### 3. Capa de Servicios (`service/`)

**AuthService**
- Servicio principal de autenticación
- Orquesta el proceso de login y generación de tokens
- Gestiona la renovación de tokens mediante refresh tokens

**UsernamePasswordAuthService**
- Implementación específica para autenticación con usuario y contraseña
- Integra con Spring Security Authentication Manager

**RefreshTokenService / RefreshTokenServiceImpl**
- Gestión del ciclo de vida de refresh tokens
- Creación, validación y revocación de tokens de refresco

**UserService**
- Gestión de usuarios en el sistema
- Implementa `UserDetailsService` para carga de usuarios por email

#### 4. Capa de Repositorios (`repository/`)

Interfaces de Spring Data MongoDB para acceso a datos:
- Repositorio de usuarios
- Repositorio de refresh tokens

#### 5. Capa de Configuración (`config/`)

**SecurityConfig**
- Configuración de Spring Security
- Define cadenas de filtros de seguridad
- Configura endpoints públicos y protegidos
- Establece políticas de CORS
- Configuración de proveedores de autenticación

#### 6. DTOs (`dto/`)

**LoginRequest**
- Objeto de transferencia para solicitudes de login
- Contiene email y password con validaciones

**AuthResponse**
- Respuesta exitosa de autenticación
- Incluye access token, refresh token y metadata

**RefreshTokenRequest**
- Solicitud para renovar access token
- Contiene el refresh token

**NewAccessTokenResponse**
- Respuesta con el nuevo access token generado

#### 7. Utilidades (`util/`)

**JwtProvider**
- Componente para generación y validación de tokens JWT
- Firma tokens con clave secreta
- Extrae información (claims) de los tokens
- Valida expiración y firma de tokens

## Sistema de Autenticación

### Flujo de Autenticación

1. **Login Inicial**
   ```
   Cliente → POST /api/v1/auth/login
           ↓ (email + password)
   AuthService → Validación de credenciales
               ↓
   Spring Security Authentication
               ↓
   Generación de JWT Access Token (1 hora)
   Generación de Refresh Token (7 días)
               ↓
   Cliente ← AuthResponse (tokens)
   ```

2. **Uso de Access Token**
   ```
   Cliente → Request con Authorization: Bearer {access_token}
           ↓
   Spring Security Filter
           ↓
   Validación JWT
           ↓
   Acceso al recurso protegido
   ```

3. **Renovación de Token**
   ```
   Cliente → POST /api/v1/auth/refresh-token
           ↓ (refresh_token)
   Validación de Refresh Token
           ↓
   Generación de nuevo Access Token
           ↓
   Cliente ← NewAccessTokenResponse
   ```

### Seguridad

- **Encriptación de Contraseñas**: Las contraseñas se almacenan encriptadas usando BCrypt
- **Firma JWT**: Los tokens JWT están firmados con una clave secreta configurable
- **Expiración de Tokens**: 
  - Access Token: 1 hora (configurable)
  - Refresh Token: 7 días (configurable)
- **OAuth2 Ready**: Preparado para integración con proveedores OAuth2 (Google, LinkedIn)

## Configuración

### Perfiles de Aplicación

El servicio soporta múltiples perfiles de ejecución:

#### Desarrollo (`application-dev.properties`)
- MongoDB local: `mongodb://localhost:27017/identity_db`
- Tokens JWT con expiración corta para pruebas
- Clave JWT predeterminada (debe cambiarse en producción)

#### Producción (`application-prod.properties`)
- Configuración basada en variables de entorno
- Variables requeridas:
  - `SERVER_PORT`: Puerto del servidor (default: 8082)
  - `SPRING_DATA_MONGODB_URI`: URI de conexión a MongoDB
  - `JWT_SECRET`: Clave secreta para firma de JWT
  - `JWT_EXPIRATION`: Tiempo de expiración del access token (ms)
  - `JWT_REFRESH_EXPIRATION`: Tiempo de expiración del refresh token (ms)

### Variables de Entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SERVER_PORT` | Puerto del servidor | `8082` |
| `SPRING_DATA_MONGODB_URI` | URI de MongoDB | `mongodb://user:pass@host:27017/db` |
| `JWT_SECRET` | Clave secreta JWT (Base64) | `7NG5umhwfjKJDAqa53A...` |
| `JWT_EXPIRATION` | Expiración access token (ms) | `3600000` (1 hora) |
| `JWT_REFRESH_EXPIRATION` | Expiración refresh token (ms) | `604800000` (7 días) |

## Endpoints API

### Autenticación

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

#### Renovar Token
```http
POST /api/v1/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Respuesta Exitosa (200 OK)**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

## Monitoreo y Observabilidad

### Actuator Endpoints

El servicio expone endpoints de Spring Boot Actuator para monitoreo:

- `/actuator/health`: Estado de salud del servicio
- `/actuator/info`: Información del servicio
- `/actuator/prometheus`: Métricas en formato Prometheus

### Métricas

El servicio está integrado con Micrometer y expone métricas para:
- Rendimiento de endpoints
- Uso de memoria y CPU
- Conexiones a MongoDB
- Tasa de autenticaciones exitosas/fallidas

## Modelo de Datos

### Colección: `user`

```json
{
  "_id": "ObjectId",
  "username": "string",
  "email": "string",
  "password": "string (encrypted)",
  "role": "string"
}
```

### Colección: `refresh_token`

```json
{
  "_id": "ObjectId",
  "token": "string",
  "userId": "string",
  "expiryDate": "datetime",
  "createdAt": "datetime"
}
```

## Integración OAuth2

El servicio está preparado para autenticación con proveedores externos:

### Proveedores Soportados
- **Google**: Autenticación mediante Google OAuth2
- **LinkedIn**: Autenticación mediante LinkedIn OAuth2

La configuración de proveedores OAuth2 está comentada en `application.properties` y puede habilitarse configurando:
- Client ID
- Client Secret
- Scopes requeridos (openid, profile, email)

## Arquitectura de Microservicios

Este servicio forma parte de una arquitectura de microservicios más amplia:

- **Celda**: Identity Cell
- **Responsabilidad**: Autenticación y gestión de identidad
- **Puerto por defecto**: 8082
- **Base de datos**: MongoDB (identity_db)

## Versionado de API

La API utiliza versionado en la URL:
- Versión actual: `v1`
- Base path: `/api/v1/auth`

## Dependencias Principales

- `spring-boot-starter-data-mongodb`: Persistencia con MongoDB
- `spring-boot-starter-security`: Framework de seguridad
- `spring-boot-starter-oauth2-client`: Cliente OAuth2
- `spring-boot-starter-web`: Framework web RESTful
- `spring-boot-starter-validation`: Validación de datos
- `spring-boot-starter-actuator`: Monitoreo y métricas
- `micrometer-registry-prometheus`: Integración con Prometheus
- `jjwt`: Librería para manejo de JWT
- `lombok`: Reducción de código boilerplate

## Consideraciones de Seguridad

1. **Nunca** exponer la clave secreta JWT (`jwt.secret`) en repositorios públicos
2. Usar claves secretas largas y complejas (mínimo 256 bits)
3. Configurar HTTPS en producción para proteger tokens en tránsito
4. Implementar rate limiting para endpoints de autenticación
5. Rotar claves JWT periódicamente
6. Implementar blacklist de tokens para logout inmediato
7. Validar y sanitizar todas las entradas de usuario
8. Mantener actualizadas las dependencias de seguridad

## Estructura del Proyecto

```
auth-service/
├── src/main/java/com/udeajobs/identity/auth_service/
│   ├── AuthServiceApplication.java      # Punto de entrada
│   ├── config/                          # Configuraciones
│   │   └── SecurityConfig.java
│   ├── controller/                      # Endpoints REST
│   │   └── AuthController.java
│   ├── dto/                             # Data Transfer Objects
│   │   ├── AuthResponse.java
│   │   ├── LoginRequest.java
│   │   ├── NewAccessTokenResponse.java
│   │   └── RefreshTokenRequest.java
│   ├── entity/                          # Entidades de dominio
│   │   ├── User.java
│   │   └── RefreshToken.java
│   ├── repository/                      # Acceso a datos
│   ├── service/                         # Lógica de negocio
│   │   ├── AuthService.java
│   │   ├── UsernamePasswordAuthService.java
│   │   ├── RefreshTokenService.java
│   │   ├── RefreshTokenServiceImpl.java
│   │   └── UserService.java
│   └── util/                            # Utilidades
│       └── JwtProvider.java
├── src/main/resources/
│   ├── application.properties           # Configuración base
│   ├── application-dev.properties       # Configuración desarrollo
│   └── application-prod.properties      # Configuración producción
├── build.gradle                         # Configuración de Gradle
└── Dockerfile                           # Containerización
```

## Tecnologías y Patrones

### Patrones de Diseño
- **MVC**: Separación de responsabilidades en capas
- **Repository Pattern**: Abstracción de acceso a datos
- **Service Layer**: Encapsulación de lógica de negocio
- **DTO Pattern**: Transferencia de datos entre capas
- **Dependency Injection**: Inyección de dependencias con Spring

### Principios SOLID
- **Single Responsibility**: Cada clase tiene una única responsabilidad
- **Dependency Inversion**: Dependencias mediante interfaces

### Buenas Prácticas
- Validación de datos con Bean Validation
- Manejo centralizado de excepciones
- Uso de Lombok para reducir boilerplate
- Configuración externalizada por perfiles
- Logs estructurados para troubleshooting

---

**Versión**: 0.0.1-SNAPSHOT  
**Grupo**: com.udeajobs.identity  
**Artifact**: auth-service
