# 🧬 Identity Cell

La **Identity Cell** es una célula del ecosistema **UdeaJobs** dedicada a todo lo relacionado con la **gestión de identidad, autenticación y autorización** de los usuarios.

Este repositorio agrupa en un solo lugar los microservicios que trabajan juntos para manejar el acceso seguro dentro de la plataforma.  
Reúne todo el código, la configuración y la orquestación necesaria para que estos servicios funcionen de manera integrada.

---

## 🌱 Propósito

La idea principal de esta célula es **centralizar** los servicios de identidad en un único repositorio, lo que facilita:

- La **colaboración** entre desarrolladores.  
- La **coherencia** en configuraciones y dependencias.  
- La **simplicidad** al desplegar y mantener los servicios.  

---

## ⚙️ Servicios incluidos

| Servicio | Descripción breve |
|-----------|------------------|
| 🧍 **Account Service** | Gestiona cuentas, perfiles y datos de usuario. |
| 🔐 **Auth Service** | Maneja la autenticación y emisión de tokens. |
| 🌐 **Identity Gateway** | Punto de entrada que enruta y unifica las peticiones hacia los demás servicios. |

---

## 🧩 Estructura del repositorio

```text
identity-cell/
├── account-service/
├── auth-service/
├── authz-service/
├── identity-gateway/
├── docker-compose.yml
└── README.md
```
Cada carpeta representa un microservicio independiente que, junto con los demás, conforma la célula de identidad.

---

## 🚀 En pocas palabras

> Esta célula permite que los usuarios de UdeaJobs puedan **crear cuentas, iniciar sesión y acceder a recursos de forma segura**.

---

## 🧾 Licencia

Proyecto desarrollado como parte del ecosistema **UdeaJobs**.  
Distribuido bajo la licencia **MIT**.

---

> _“Una identidad sólida es la base de una experiencia segura.”_
