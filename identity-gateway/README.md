# Identity Gateway

## Descripción

Identity Gateway es el API Gateway de la célula de identidad (Identity Cell) del proyecto UdeAJobs. Actúa como punto de entrada unificado para todos los microservicios relacionados con la gestión de identidad y autenticación del sistema.

Este gateway proporciona enrutamiento inteligente, balanceo de carga y un punto centralizado de acceso a los servicios de la célula de identidad, siguiendo el patrón arquitectónico de microservicios.

## Tecnologías

- **Java 21**: Lenguaje de programación principal
- **Spring Boot 3.5.5**: Framework base de la aplicación
- **Spring Cloud Gateway**: Implementación del API Gateway reactivo
- **Spring Cloud LoadBalancer**: Balanceo de carga del lado del cliente
- **Spring Boot Actuator**: Monitoreo y métricas de la aplicación
- **Micrometer Prometheus**: Exportación de métricas para Prometheus
- **Lombok**: Reducción de código boilerplate
- **Gradle**: Herramienta de construcción y gestión de dependencias

## Arquitectura

### Patrón API Gateway

Este servicio implementa el patrón API Gateway, que actúa como punto de entrada único para los clientes, enrutando las peticiones a los microservicios correspondientes. Los beneficios incluyen:

- **Desacoplamiento**: Los clientes no necesitan conocer la ubicación de cada microservicio
- **Punto único de entrada**: Simplifica la configuración del cliente y la seguridad
- **Enrutamiento centralizado**: Facilita la gestión de rutas y versionado de APIs
- **Monitoreo unificado**: Permite observar todo el tráfico desde un punto central

### Microservicios Gestionados

El gateway gestiona el enrutamiento hacia tres microservicios de la célula de identidad:

#### 1. Account Service (Puerto 8081)
- **Ruta**: `/identity/api/v1/accounts/**`
- **Función**: Gestión de cuentas de usuario
- **URI Interna**: `http://account-service:8081`

#### 2. Auth Service (Puerto 8082)
- **Ruta**: `/identity/api/v1/auth/**`
- **Función**: Autenticación de usuarios
- **URI Interna**: `http://auth-service:8082`

#### 3. Authz Service (Puerto 8083)
- **Ruta**: `/identity/api/v1/authz/**`
- **Función**: Autorización y gestión de permisos
- **URI Interna**: `http://authz-service:8083`

## Configuración

### Puerto del Servidor

El gateway se ejecuta en el puerto **8080** y expone todas las rutas bajo el prefijo `/identity/api/v1/`.

### Discovery Client

El servicio utiliza Spring Cloud Discovery Client en modo simple (simple instances) para el descubrimiento de servicios. Cada microservicio está configurado con su URI correspondiente, permitiendo que el gateway los localice y enrute el tráfico adecuadamente.

### Enrutamiento

Todas las rutas están configuradas con:
- **Load Balancer**: Utiliza el esquema `lb://` para balanceo de carga
- **Filtro StripPrefix**: Elimina el primer segmento del path (`/identity`) antes de reenviar la petición al microservicio
- **Predicados de Path**: Coincidencia de rutas basada en patrones

#### Ejemplo de Flujo de Enrutamiento

```
Cliente → /identity/api/v1/accounts/user/123
         ↓
      Gateway (Puerto 8080)
         ↓
      StripPrefix(1) → /api/v1/accounts/user/123
         ↓
   Account Service (Puerto 8081)
```

## Monitoreo y Observabilidad

### Spring Boot Actuator

El gateway expone varios endpoints de Actuator para monitoreo y administración:

- **`/actuator/health`**: Estado de salud del servicio
- **`/actuator/info`**: Información del servicio
- **`/actuator/metrics`**: Métricas de la aplicación
- **`/actuator/prometheus`**: Métricas en formato Prometheus
- **`/actuator/gateway`**: Información específica del gateway (rutas, filtros, predicados)
- **`/actuator/refresh`**: Recarga de configuración sin reiniciar

### Métricas

Las métricas se exportan en formato Prometheus a través de Micrometer, permitiendo la integración con sistemas de monitoreo como:
- Prometheus para recolección de métricas
- Grafana para visualización de dashboards
- Alertmanager para notificaciones

## Estructura del Proyecto

```
identity-gateway/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/identity_cell/cell_gateway/
│   │   │       └── CellGatewayApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── java/
│           └── com/identity_cell/cell_gateway/
│               └── CellGatewayApplicationTests.java
├── build.gradle
├── settings.gradle
├── Dockerfile
└── README.md
```

## Dockerización

El proyecto incluye un `Dockerfile` que permite la containerización del gateway, facilitando su despliegue en entornos de contenedores y orquestadores como Kubernetes o Docker Compose.

## Versionado

- **Versión Actual**: 0.0.1-SNAPSHOT
- **Spring Cloud Version**: 2025.0.0

## Dependencias Principales

### Runtime
- `spring-boot-starter-actuator`: Monitoreo y gestión
- `spring-cloud-starter-gateway`: Núcleo del API Gateway
- `spring-cloud-starter-loadbalancer`: Balanceo de carga
- `micrometer-registry-prometheus`: Exportación de métricas

### Development
- `spring-boot-devtools`: Herramientas de desarrollo
- `lombok`: Generación de código

### Testing
- `spring-boot-starter-test`: Framework de testing
- `junit-platform-launcher`: Ejecución de tests

## Características Técnicas

### Programación Reactiva

Spring Cloud Gateway está construido sobre Spring WebFlux y Project Reactor, lo que significa que:
- Todas las operaciones son no bloqueantes
- Alto rendimiento y escalabilidad
- Manejo eficiente de múltiples conexiones concurrentes

### Balanceo de Carga

El gateway implementa balanceo de carga del lado del cliente mediante Spring Cloud LoadBalancer, distribuyendo las peticiones entre múltiples instancias de cada microservicio (si están disponibles).

### Resiliencia

Aunque no se muestra en la configuración actual, el gateway puede extenderse fácilmente con patrones de resiliencia como:
- Circuit Breaker
- Retry
- Rate Limiting
- Request Timeout

## Convenciones de Nomenclatura

- **Artifact ID**: `cell-gateway`
- **Group ID**: `com.identity-cell`
- **Package Base**: `com.identity_cell.cell_gateway`
- **Application Name**: `cell-gateway`


