# Account Service - UdeAJobs

## Descripción General

**Account Service** es un microservicio de gestión de cuentas de usuario que forma parte del ecosistema de microservicios de UdeAJobs. Este servicio es responsable de manejar todo el ciclo de vida de las cuentas de usuario, incluyendo registro, verificación por email, recuperación de contraseñas y gestión de autenticación básica.

El servicio está desarrollado con **Spring Boot 3.5.5** y **Java 21**, utilizando una arquitectura de microservicios con comunicación asíncrona a través de eventos.

---

## Arquitectura

### Stack Tecnológico

- **Framework:** Spring Boot 3.5.5
- **Lenguaje:** Java 21
- **Base de Datos:** MongoDB
- **Gestor de Mensajería:** RabbitMQ
- **Email:** Spring Mail con SMTP (Gmail)
- **Motor de Plantillas:** Thymeleaf
- **Métricas:** Micrometer con Prometheus
- **Seguridad:** Spring Security
- **Validación:** Jakarta Validation
- **Build Tool:** Gradle

### Patrones de Diseño

- **Arquitectura en Capas:** Separación clara entre Controller, Service, Repository
- **Event-Driven Architecture:** Publicación de eventos mediante RabbitMQ
- **Repository Pattern:** Abstracción de acceso a datos con Spring Data MongoDB
- **DTO Pattern:** Transferencia de datos mediante objetos específicos

---

## Estructura del Proyecto

```
account-service/
├── src/main/java/com/udeajobs/identity/account_service/
│   ├── AccountServiceApplication.java    # Clase principal de la aplicación
│   ├── config/                            # Configuraciones de la aplicación
│   │   ├── RabbitMQConfig.java           # Configuración de RabbitMQ
│   │   └── SecurityConfig.java           # Configuración de seguridad
│   ├── controller/                        # Controladores REST
│   │   └── AccountController.java        # API endpoints para cuentas
│   ├── dto/                               # Data Transfer Objects
│   │   ├── ErrorResponse.java            # DTO para respuestas de error
│   │   ├── ForgotPasswordRequest.java    # DTO para solicitud de recuperación
│   │   ├── RegistrationRequest.java      # DTO para registro de usuario
│   │   ├── ResetPasswordRequest.java     # DTO para reseteo de contraseña
│   │   └── VerificationRequest.java      # DTO para verificación de cuenta
│   ├── entity/                            # Entidades de dominio
│   │   └── User.java                     # Entidad de usuario
│   ├── enums/                             # Enumeraciones
│   │   ├── ROLE.java                     # Roles del sistema
│   │   └── STATUS.java                   # Estados de usuario
│   ├── events/                            # Eventos del dominio
│   │   └── CuentaVerificadaEvent.java   # Evento de cuenta verificada
│   ├── exception/                         # Excepciones personalizadas
│   ├── repository/                        # Repositorios de datos
│   │   └── UserRepository.java           # Repositorio de usuarios
│   └── service/                           # Lógica de negocio
│       ├── interfaces/                    # Interfaces de servicios
│       │   ├── AccountService.java       # Interfaz del servicio de cuentas
│       │   └── MailService.java          # Interfaz del servicio de email
│       └── implementation/                # Implementaciones de servicios
└── src/main/resources/
    ├── application.properties             # Configuración base
    ├── application-dev.properties         # Configuración de desarrollo
    ├── application-prod.properties        # Configuración de producción
    └── templates/                         # Plantillas de email
        ├── verification-email.html        # Plantilla de verificación
        └── password-reset-link.html       # Plantilla de recuperación
```

---

## Modelo de Datos

### Entidad User

Representa un usuario en el sistema con los siguientes atributos:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | String | Identificador único del usuario (MongoDB ObjectId) |
| `fullName` | String | Nombre completo del usuario |
| `username` | String | Nombre de usuario único |
| `email` | String | Correo electrónico (único) |
| `password` | String | Contraseña encriptada con BCrypt |
| `role` | ROLE | Rol del usuario (FREELANCER o EMPLOYER) |
| `status` | STATUS | Estado de la cuenta (PENDING_VERIFICATION, ACTIVE, INACTIVE) |
| `verificationCode` | String | Código temporal para verificación de cuenta |
| `resetPasswordToken` | String | Token temporal para recuperación de contraseña |
| `resetPasswordTokenExpiration` | LocalDateTime | Fecha de expiración del token de recuperación |

### Enumeraciones

#### ROLE
- `FREELANCER`: Usuario que ofrece servicios
- `EMPLOYER`: Usuario que contrata servicios

#### STATUS
- `PENDING_VERIFICATION`: Cuenta creada pendiente de verificación por email
- `ACTIVE`: Cuenta verificada y activa
- `INACTIVE`: Cuenta desactivada

---

## API Endpoints

Base URL: `/api/v1/accounts`

### 1. Registro de Usuario

**Endpoint:** `POST /api/v1/accounts/register`

**Descripción:** Registra una nueva cuenta de usuario en el sistema.

**Request Body:**
```json
{
  "fullName": "Juan Pérez",
  "username": "juanperez",
  "email": "juan@example.com",
  "password": "SecurePass123!",
  "role": "FREELANCER"
}
```

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "fullName": "Juan Pérez",
  "username": "juanperez",
  "email": "juan@example.com",
  "role": "FREELANCER",
  "status": "PENDING_VERIFICATION"
}
```

**Proceso:**
1. Valida que el email no esté registrado
2. Encripta la contraseña con BCrypt
3. Genera un código de verificación de 6 dígitos
4. Guarda el usuario con estado `PENDING_VERIFICATION`
5. Envía email de verificación con el código

---

### 2. Verificación de Cuenta

**Endpoint:** `POST /api/v1/accounts/verify`

**Descripción:** Verifica una cuenta de usuario usando el código enviado por email.

**Request Body:**
```json
{
  "email": "juan@example.com",
  "code": "123456"
}
```

**Response:** `200 OK`
```json
"User verified successfully"
```

**Proceso:**
1. Valida que el usuario exista
2. Verifica que el código coincida
3. Cambia el estado a `ACTIVE`
4. Elimina el código de verificación
5. Publica evento `CuentaVerificadaEvent` a RabbitMQ

---

### 3. Solicitud de Recuperación de Contraseña

**Endpoint:** `POST /api/v1/accounts/forgot-password`

**Descripción:** Inicia el proceso de recuperación de contraseña.

**Request Body:**
```json
{
  "email": "juan@example.com"
}
```

**Response:** `200 OK`
```json
"Password reset code sent to your email"
```

**Proceso:**
1. Valida que el usuario exista
2. Genera un token UUID único
3. Establece tiempo de expiración (1 hora)
4. Guarda el token en la base de datos
5. Envía email con enlace de recuperación

---

### 4. Restablecimiento de Contraseña

**Endpoint:** `POST /api/v1/accounts/reset-password`

**Descripción:** Restablece la contraseña usando el token de recuperación.

**Request Body:**
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "newPassword": "NewSecurePass123!"
}
```

**Response:** `200 OK`
```json
"Password reset successfully"
```

**Proceso:**
1. Busca usuario por token
2. Valida que el token no haya expirado
3. Encripta la nueva contraseña
4. Actualiza la contraseña en la base de datos
5. Elimina el token de recuperación

---

## Servicios Internos

### AccountService

Servicio principal que maneja toda la lógica de negocio relacionada con cuentas:

- `registerUser(User user)`: Registra un nuevo usuario
- `verifyUser(String email, String code)`: Verifica una cuenta
- `forgotPassword(String email)`: Inicia recuperación de contraseña
- `resetPassword(String token, String newPassword)`: Restablece contraseña

### MailService

Servicio responsable del envío de correos electrónicos:

- `sendVerificationEmail(String to, String code)`: Envía email de verificación
- `sendPasswordResetEmail(String to, String token)`: Envía email de recuperación

Utiliza plantillas Thymeleaf para generar emails HTML personalizados.

---

## Sistema de Eventos

### RabbitMQ Configuration

**Exchange:** `udea.identity.account-service` (Topic Exchange)

### Eventos Publicados

#### CuentaVerificadaEvent

Publicado cuando un usuario verifica exitosamente su cuenta.

**Routing Key:** `account.verified`

**Payload:**
```json
{
  "userId": "507f1f77bcf86cd799439011",
  "email": "juan@example.com",
  "role": "FREELANCER",
  "timestamp": "2025-10-14T10:30:00"
}
```

Este evento permite que otros microservicios reaccionen a la verificación de cuentas (ejemplo: crear perfiles, enviar notificaciones de bienvenida).

---

## Configuración

### Variables de Entorno

#### Desarrollo (application-dev.properties)
```properties
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/identity_db
app.reset-url-base=http://localhost:4200/reset-password
```

#### Producción (application-prod.properties)
```properties
SERVER_PORT                    # Puerto del servidor (default: 8081)
SPRING_DATA_MONGODB_URI        # URI de conexión a MongoDB
EMAIL_USERNAME                 # Usuario SMTP de Gmail
EMAIL_PASSWORD                 # Contraseña de aplicación de Gmail
APP_RESET_PASSWORD_URL_BASE    # URL base para enlaces de recuperación
RABBITMQ_HOST                  # Host del servidor RabbitMQ
```

### Configuración de Email

El servicio utiliza Gmail SMTP con las siguientes configuraciones:

- **Host:** smtp.gmail.com
- **Puerto:** 587
- **TLS:** Habilitado
- **Autenticación:** Requerida

**Nota:** Para Gmail es necesario usar una contraseña de aplicación en lugar de la contraseña normal.

---

## Seguridad

### Encriptación de Contraseñas

Las contraseñas se encriptan usando **BCryptPasswordEncoder** de Spring Security antes de almacenarse en la base de datos.

### Tokens de Recuperación

- Los tokens son UUID v4 aleatorios
- Tiempo de expiración: 1 hora
- Se eliminan después de ser utilizados
- Validación de expiración en cada uso

### Códigos de Verificación

- Códigos numéricos de 6 dígitos
- Generados aleatoriamente
- Se eliminan después de la verificación

---

## Monitoreo y Observabilidad

### Actuator Endpoints

El servicio expone endpoints de Spring Actuator para monitoreo:

- `/actuator/health` - Estado de salud del servicio
- `/actuator/info` - Información del servicio
- `/actuator/prometheus` - Métricas en formato Prometheus

### Métricas

Se utiliza **Micrometer** con **Prometheus** para recolección de métricas de rendimiento y operación del servicio.

### Logging

El servicio utiliza **SLF4J** con **Logback** para logging estructurado:

- Logs de registro de usuarios
- Logs de verificación de cuentas
- Logs de recuperación de contraseñas
- Logs de errores y excepciones

---

## Dependencias Principales

```gradle
dependencies {
    // Spring Boot Core
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    
    // Base de Datos
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
    
    // Mensajería
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
    
    // Email
    implementation 'org.springframework.boot:spring-boot-starter-mail'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    
    // Monitoreo
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'io.micrometer:micrometer-registry-prometheus'
    
    // Utilidades
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

---

## Flujos de Trabajo

### Flujo de Registro y Verificación

```
1. Usuario → POST /register
2. Sistema valida datos
3. Sistema encripta contraseña
4. Sistema genera código de verificación
5. Sistema guarda usuario (status: PENDING_VERIFICATION)
6. Sistema envía email con código
7. Usuario recibe email
8. Usuario → POST /verify con código
9. Sistema valida código
10. Sistema actualiza status a ACTIVE
11. Sistema publica CuentaVerificadaEvent a RabbitMQ
12. Otros microservicios procesan el evento
```

### Flujo de Recuperación de Contraseña

```
1. Usuario → POST /forgot-password
2. Sistema valida que el usuario exista
3. Sistema genera token UUID
4. Sistema establece expiración (1 hora)
5. Sistema guarda token en BD
6. Sistema envía email con enlace + token
7. Usuario hace clic en enlace
8. Frontend → POST /reset-password con token y nueva contraseña
9. Sistema valida token y expiración
10. Sistema encripta nueva contraseña
11. Sistema actualiza contraseña
12. Sistema elimina token
```

---

## Integración con Microservicios

Este servicio forma parte de la célula de identidad (identity-cell) del ecosistema UdeAJobs y se comunica con otros servicios mediante:

### Comunicación Asíncrona (RabbitMQ)

- **Publica eventos:** CuentaVerificadaEvent
- **Exchange:** udea.identity.account-service
- **Patrón:** Topic Exchange para routing flexible

### Otros Servicios Esperados

Aunque no se implementan en este servicio, se espera integración con:

- **Profile Service:** Consumiría CuentaVerificadaEvent para crear perfiles
- **Notification Service:** Enviaría notificaciones de bienvenida
- **Authentication Service:** Manejaría JWT y sesiones

---

## Consideraciones de Desarrollo

### Perfiles de Spring

- **dev:** Desarrollo local con MongoDB local
- **prod:** Producción con configuración mediante variables de entorno

### Plantillas de Email

Las plantillas HTML están ubicadas en `src/main/resources/templates/`:

- `verification-email.html`: Diseño del email de verificación
- `password-reset-link.html`: Diseño del email de recuperación

Utilizan Thymeleaf para interpolación de variables dinámicas.

---

## Versionamiento

**Versión Actual:** 0.0.1-SNAPSHOT

**API Version:** v1

El servicio sigue versionamiento semántico y la API REST incluye el prefijo de versión `/api/v1/` para mantener compatibilidad hacia atrás.


---

**Proyecto**: UdeAJobs  
**Célula**: Identity Cell  
**Servicio**: Authorization Service



