# 📚 Tintero Social

> Plataforma social de lectura — DSY1103 · DuocUC

Tintero Social es una aplicación backend basada en **microservicios** que permite a los usuarios gestionar sus lecturas, descubrir libros, escribir reseñas y conectar con otros lectores. Construida con Java 21 y Spring Boot 4.

---

## 🏗️ Arquitectura

El proyecto está organizado como un **monorepo Maven multi-módulo**, donde cada módulo representa un microservicio independiente con su propia responsabilidad:

| Servicio | Puerto | Descripción |
|---|---|---|
| `user-service` | 8081 | Gestión de usuarios, autenticación y perfiles |
| `book-service` | 8082 | Catálogo de libros e información bibliográfica |
| `review-service` | 8083 | Reseñas y valoraciones de libros |
| `reading-list-service` | 8084 | Listas de lectura personales (leídos, leyendo, pendientes) |
| `social-service` | 8085 | Seguimiento entre usuarios, actividad social |
| `recommendation-service` | 8086 | Motor de recomendaciones personalizadas |
| `notification-service` | 8087 | Notificaciones y alertas a usuarios |
| `search-service` | 8088 | Búsqueda de libros y usuarios |
| `stats-service` | 8089 | Estadísticas de lectura y actividad |
| `audit-service` | 8090 | Registro de auditoría de acciones del sistema |
| `gateway-service` | 8080 | API Gateway (Spring Cloud Gateway) — Centraliza y enruta peticiones a los 10 microservicios |

### API Gateway

El `gateway-service` (puerto `8080`) centraliza el enrutamiento mediante **Spring Cloud Gateway**. Todas las rutas siguen el patrón `/api/{servicio}/**` y redirigen al microservicio correspondiente:

```
GET /api/users/1        → http://user-service:8081/api/users/1
GET /api/books/         → http://book-service:8082/api/books/
POST /api/reviews/      → http://review-service:8083/api/reviews/
GET /api/lists/user/1   → http://reading-list-service:8084/api/lists/user/1
... y así para los 10 servicios.
```

### Documentación Swagger / OpenAPI

Cada microservicio expone su propia documentación interactiva **Swagger UI**:

```
http://localhost:{puerto}/swagger-ui/index.html
```

Ejemplo: `http://localhost:8081/swagger-ui/index.html` para `user-service`.

---

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.4.0**
- **Spring Cloud 2024.0.0** (Gateway, RestClient)
- **Maven** (estructura multi-módulo)
- **MySQL 8** (bases de datos por servicio, perfil `prod`)
- **PostgreSQL** (base de datos en Neon, perfil `neon`)
- **H2** (base de datos en memoria para perfil `dev`)
- **JUnit 5 + Mockito** (pruebas unitarias)
- **Springdoc OpenAPI / Swagger UI** (documentación interactiva)
- **Spring Security + JWT** (autenticación en user-service)
- **Spring HATEOAS** (enlaces HAL en respuestas)
- **Docker / Docker Compose** (despliegue local contenerizado)

---

## 🚀 Inicio rápido

### Requisitos previos

- Java 21+
- Maven 3.9+ (o usar el wrapper incluido `./mvnw`)
- Docker y Docker Compose (para despliegue contenerizado)

### Clonar el repositorio

```bash
git clone https://github.com/Javi-EP/tintero-social.git
cd tintero-social
```

### Compilar todos los módulos

```bash
./mvnw clean install -DskipTests
```

### Ejecución local (desde IDE / terminal)

Cada microservicio se ejecuta de forma independiente. Por defecto usan el perfil `dev` con base de datos H2 en memoria:

```bash
cd user-service
../mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Ejecución con Docker (local)

```bash
docker compose up --build
```

Esto levanta **10 bases de datos MySQL** (una por servicio), **10 microservicios** y el **API Gateway**, todos con el perfil `prod`.

### Perfiles de ejecución

| Perfil | Base de datos | Uso |
|---|---|---|
| `dev` | H2 en memoria | Desarrollo local |
| `prod` | MySQL 8.x (vía Docker Compose) | Ejecución con Docker local |
| `neon` | PostgreSQL (vía Neon) | Deploy en Render |

### Despliegue en Render + Neon

El proyecto se despliega en **Render** como un solo **Web Service Docker** gratuito que ejecuta los 6 microservicios esenciales dentro de un mismo contenedor, usando **Neon** como base de datos PostgreSQL.

#### Requisitos en Neon

1. Crear un proyecto en [neon.tech](https://neon.tech)
2. Obtener la cadena de conexión: `postgresql://user:pass@host/db?sslmode=require`

#### Variables de entorno en Render

| Variable | Valor |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `neon` |
| `DB_HOST` | Host de Neon (ej: `ep-xxx.us-east-2.aws.neon.tech`) |
| `DB_PORT` | `5432` |
| `DB_NAME` | `neondb` |
| `DB_USER` | Usuario de Neon |
| `DB_PASSWORD` | Contraseña de Neon |

Render expone automáticamente el puerto `8080` (Gateway). Los microservicios se comunican por `localhost` dentro del contenedor.

#### Servicios desplegados

| Servicio | Puerto interno |
|---|---|
| `user-service` | 8081 |
| `book-service` | 8082 |
| `review-service` | 8083 |
| `reading-list-service` | 8084 |
| `social-service` | 8085 |
| `gateway-service` | 8080 |

> Los servicios `notification-service`, `recommendation-service`, `search-service`, `stats-service` y `audit-service` se excluyen del deploy para ajustarse al límite de memoria del plan gratuito de Render (512 MB).

---

## 📁 Estructura del proyecto

```
tintero-social/
├── audit-service/           # Registro de auditoría
├── book-service/            # Catálogo de libros
├── gateway-service/         # API Gateway (Spring Cloud Gateway)
├── notification-service/    # Notificaciones a usuarios
├── reading-list-service/    # Listas de lectura personales
├── recommendation-service/  # Motor de recomendaciones
├── review-service/          # Reseñas y valoraciones
├── search-service/          # Búsqueda de libros y usuarios
├── social-service/          # Seguimiento y actividad social
├── stats-service/           # Estadísticas de lectura
├── user-service/            # Gestión de usuarios y autenticación
├── compose.yml              # Orquestación Docker (MySQL)
├── Dockerfile               # Build para Render (single container)
├── docker-entrypoint.sh     # Entrypoint para Render
├── .dockerignore            # Exclusiones para Docker
├── pom.xml                  # POM raíz (multi-módulo)
├── mvnw                     # Maven Wrapper (Linux/macOS)
└── mvnw.cmd                 # Maven Wrapper (Windows)
```

---

## 👥 Equipo

Proyecto académico desarrollado para el curso **DSY1103** de [DuocUC](https://www.duoc.cl).

- **Javier Escalona Padilla** — Desarrollo de microservicios, API Gateway, Docker, pruebas unitarias
- **Eloy Contreras** — Desarrollo de microservicios, documentación Swagger, configuración YAML

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos.
