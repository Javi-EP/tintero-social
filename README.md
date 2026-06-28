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
- **Spring Boot 4.0.6**
- **Spring Cloud 2024.0.0** (Gateway, RestClient)
- **Maven** (estructura multi-módulo)
- **MySQL 8** (bases de datos por servicio)
- **H2** (base de datos en memoria para perfil dev)
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

Esto levanta 5 bases de datos MySQL, 5 microservicios (`user-service`, `book-service`, `review-service`, `reading-list-service`, `social-service`) y el `gateway-service`. Los servicios se despliegan con el perfil `prod` y variables de entorno para conectar a sus bases MySQL.

> Los servicios restantes (`notification-service`, `recommendation-service`, `search-service`, `stats-service`, `audit-service`) requieren Dockerfile propio para integrarse al ecosistema completo.

### Despliegue en Render + Neon (remoto — próximamente)

Cada microservicio se desplegará como servicio independiente en **Render**, conectándose a bases de datos MySQL en **Neon**. Las variables de entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`) se configurarán en el panel de cada servicio.

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
├── compose.yml              # Orquestación Docker
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
