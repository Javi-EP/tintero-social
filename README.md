# Tintero Social

> Plataforma social de lectura — DSY1103 · DuocUC

## Descripcion del problema

Los lectores frecuentes carecen de una plataforma integrada que les permita organizar sus lecturas, descubrir libros recomendados por la comunidad, escribir resenas y conectar con otros lectores con gustos similares. Las soluciones existentes suelen ser dispersas: aplicaciones para catalogar libros, redes sociales genericas o foros sin estructura.

## Descripcion de la solucion

Tintero Social es una aplicacion backend basada en **microservicios** que permite a los usuarios:

- Registrarse e iniciar sesion (autenticacion JWT)
- Gestionar un catalogo de libros
- Escribir resenas y valorar libros con estrellas
- Crear listas de lectura personalizadas (leidos, leyendo, pendientes)
- Seguir a otros lectores y ver su actividad
- Recibir recomendaciones personalizadas de libros
- Recibir notificaciones de actividad relevante
- Buscar libros y usuarios
- Consultar estadisticas de lectura personal

---

## Arquitectura

El proyecto esta organizado como un **monorepo Maven multi-modulo**, donde cada modulo representa un microservicio independiente con su propia responsabilidad.

### Componentes

```
                         ┌─────────────────┐
                         │  API Gateway    │
                         │  (puerto 8080)  │
                         └────────┬────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    │             │             │
              ┌─────┴─────┐ ┌────┴────┐ ┌──────┴──────┐
              │  Eureka   │ │ Services│ │   Neon /    │
              │  Server   │ │ (10)    │ │   MySQL     │
              │  (8761)   │ │         │ │   (BDs)     │
              └───────────┘ └─────────┘ └─────────────┘
```

### Listado de microservicios

| Servicio | Puerto | Descripcion |
|---|---|---|
| `eureka-server` | 8761 | Servidor de descubrimiento de servicios (Netflix Eureka) |
| `user-service` | 8081 | Gestion de usuarios, autenticacion y perfiles |
| `book-service` | 8082 | Catalogo de libros e informacion bibliografica |
| `review-service` | 8083 | Resenas y valoraciones de libros |
| `reading-list-service` | 8084 | Listas de lectura personales (leidos, leyendo, pendientes) |
| `social-service` | 8085 | Seguimiento entre usuarios, actividad social |
| `recommendation-service` | 8086 | Motor de recomendaciones personalizadas |
| `notification-service` | 8087 | Notificaciones y alertas a usuarios |
| `search-service` | 8088 | Busqueda de libros y usuarios |
| `stats-service` | 8089 | Estadisticas de lectura y actividad |
| `audit-service` | 8090 | Registro de auditoria de acciones del sistema |
| `gateway-service` | 8080 | API Gateway (Spring Cloud Gateway) |

### API Gateway

El `gateway-service` (puerto `8080`) centraliza el enrutamiento mediante **Spring Cloud Gateway** y **Netflix Eureka** para descubrimiento de servicios. En perfiles `dev` y `prod`, las rutas usan `lb://service-name` para resolver URLs via Eureka. En el perfil `neon` (Render), se usan URLs directas.

Todas las rutas siguen el patron `/api/{servicio}/**`:

```
GET  /api/users/1          → lb://user-service
GET  /api/books/           → lb://book-service
POST /api/reviews/         → lb://review-service
GET  /api/lists/user/1     → lb://reading-list-service
GET  /api/social/feed/1    → lb://social-service
GET  /api/recommendations/ → lb://recommendation-service
GET  /api/notifications/   → lb://notification-service
GET  /api/search/          → lb://search-service
GET  /api/stats/           → lb://stats-service
GET  /api/audit/           → lb://audit-service
```

### Documentacion Swagger / OpenAPI

Cada microservicio (excepto gateway y eureka) expone su propia documentacion interactiva **Swagger UI**:

```
http://localhost:{puerto}/swagger-ui/index.html
```

Ejemplo: `http://localhost:8081/swagger-ui/index.html` para `user-service`.

---

## Base de datos por servicio

Cada microservicio tiene su propia base de datos aislada:

| Servicio | BD MySQL (prod) | BD Neon/PostgreSQL (neon) | Tabla(s) |
|---|---|---|---|
| user-service | `db_users` | schema: `users` | `users` |
| book-service | `db_books` | schema: `books` | `books` |
| review-service | `db_reviews` | schema: `reviews` | `reviews`, `votes` |
| reading-list-service | `db_reading_lists` | schema: `reading_lists` | `reading_lists`, `reading_list_items` |
| social-service | `db_social` | schema: `social` | `follows` |
| recommendation-service | `db_recommendations` | schema: `recommendations` | `recommendations`, `genre_preferences` |
| notification-service | `db_notifications` | schema: `notifications` | `notifications` |
| search-service | `db_searchs` | schema: `searchs` | `book_index`, `user_index` |
| stats-service | `db_stats` | schema: `stats` | `stat_snapshots` |
| audit-service | `db_audit` | schema: `audit` | `audit_event` |

---

## Variables de entorno

| Variable | Perfil | Descripcion | Ejemplo |
|---|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Todos | Profile activo: `dev`, `prod` o `neon` | `dev` |
| `DB_HOST` | prod, neon | Host de la base de datos | `mysql-users` / `ep-xxx.neon.tech` |
| `DB_PORT` | prod, neon | Puerto de la base de datos | `3306` (MySQL) / `5432` (PostgreSQL) |
| `DB_NAME` | prod, neon | Nombre de la base de datos | `db_users` / `neondb` |
| `DB_USER` | prod, neon | Usuario de la base de datos | `tintero` |
| `DB_PASSWORD` | prod, neon | Contrasena de la base de datos | `tintero123` |
| `DB_SCHEMA` | neon | Schema de PostgreSQL | `users`, `books` |
| `EUREKA_URI` | Todos | URL del servidor Eureka | `http://localhost:8761/eureka/` |

> Ver archivo `.env.example` en la raiz del proyecto para una referencia completa.

---

## Tecnologias

- **Java 21**
- **Spring Boot 3.4.0**
- **Spring Cloud 2024.0.0** (Gateway, RestClient, Eureka)
- **Netflix Eureka** (descubrimiento de servicios)
- **Maven** (estructura multi-modulo)
- **MySQL 8** (bases de datos por servicio, perfil `prod`)
- **PostgreSQL** (base de datos en Neon, perfil `neon`)
- **H2** (base de datos en memoria para perfil `dev`)
- **Flyway** (migraciones SQL en perfil `prod`)
- **JUnit 5 + Mockito** (pruebas unitarias)
- **Springdoc OpenAPI / Swagger UI** (documentacion interactiva)
- **Spring Security + JWT** (autenticacion en user-service)
- **Spring HATEOAS** (enlaces HAL en respuestas)
- **Docker / Docker Compose** (despliegue local contenerizado)

---

## Inicio rapido

### Requisitos previos

- Java 21+
- Maven 3.9+ (o usar el wrapper incluido `./mvnw`)
- Docker y Docker Compose (para despliegue contenerizado)

### Clonar el repositorio

```bash
git clone https://github.com/Javi-EP/tintero-social.git
cd tintero-social
```

### Compilar todos los modulos

```bash
./mvnw clean install -DskipTests
```

### Ejecucion local (terminal)

Cada microservicio se ejecuta de forma independiente. Por defecto usan el perfil `dev` con base de datos H2 en memoria.

**Linux / macOS:**

```bash
# Arrancar Eureka Server primero
./mvnw spring-boot:run -pl eureka-server

# En otra terminal, arrancar los microservicios
./mvnw spring-boot:run -pl user-service
./mvnw spring-boot:run -pl book-service
./mvnw spring-boot:run -pl review-service
./mvnw spring-boot:run -pl reading-list-service
./mvnw spring-boot:run -pl social-service
./mvnw spring-boot:run -pl recommendation-service
./mvnw spring-boot:run -pl notification-service
./mvnw spring-boot:run -pl search-service
./mvnw spring-boot:run -pl stats-service
./mvnw spring-boot:run -pl audit-service

# Arrancar el Gateway al final
./mvnw spring-boot:run -pl gateway-service
```

**Windows:**

```cmd
mvnw.cmd spring-boot:run -pl eureka-server
mvnw.cmd spring-boot:run -pl user-service
mvnw.cmd spring-boot:run -pl gateway-service
:: ... y asi para cada servicio
```

### Orden de arranque de servicios

```
1. eureka-server        (8761)  ← Arrancar primero
2. Microservicios       (8081-8090) ← Cualquier orden, se registran automaticamente
3. gateway-service      (8080)  ← Arrancar despues de los demas
```

### Ejecucion con Docker (local)

```bash
docker compose up --build
```

Esto levanta:

- **10 bases de datos MySQL** (una por servicio)
- **1 servidor Eureka** (`eureka-server`)
- **10 microservicios**
- **1 API Gateway**

Total: **22 contenedores**.

### Ejecutar pruebas

```bash
# Todas las pruebas de todos los modulos
./mvnw test

# Pruebas de un servicio especifico
./mvnw test -pl user-service
./mvnw test -pl book-service

# Windows
mvnw.cmd test
mvnw.cmd test -pl user-service
```

---

## Perfiles de ejecucion

| Perfil | Base de datos | Flyway | Eureka | Uso |
|---|---|---|---|---|
| `dev` | H2 en memoria | Desactivado | Activo | Desarrollo local |
| `prod` | MySQL 8.x (Docker) | Activado | Activo | Ejecucion con Docker local |
| `neon` | PostgreSQL (Neon) | Desactivado | Desactivado | Deploy en Render |

---

## Despliegue en Render + Neon

El proyecto se despliega en **Render** como un solo **Web Service Docker** que ejecuta los 6 microservicios esenciales dentro de un mismo contenedor, usando **Neon** como base de datos PostgreSQL.

### URL publica

**https://tintero-social.onrender.com**

### Servicios desplegados

| Servicio | Puerto interno | Rol |
|---|---|---|
| `gateway-service` | 8080 | API Gateway (punto de entrada) |
| `user-service` | 8081 | Gestion de usuarios y autenticacion |
| `book-service` | 8082 | Catalogo de libros |
| `review-service` | 8083 | Resenas y valoraciones |
| `reading-list-service` | 8084 | Listas de lectura |
| `social-service` | 8085 | Seguimiento y actividad social |

### Servicios excluidos de Render

Los siguientes servicios se excluyen del deploy para ajustarse al limite de memoria del plan gratuito (512 MB):

- `notification-service`
- `recommendation-service`
- `search-service`
- `stats-service`
- `audit-service`

### Variables de entorno en Render

| Variable | Valor |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `neon` |
| `DB_HOST` | Host de Neon (ej: `ep-xxx.us-east-2.aws.neon.tech`) |
| `DB_PORT` | `5432` |
| `DB_NAME` | `neondb` |
| `DB_USER` | Usuario de Neon |
| `DB_PASSWORD` | Contrasena de Neon |

> En el perfil `neon`, Eureka se desactiva y el Gateway usa URLs directas (`localhost`) ya que todos los servicios corren dentro del mismo contenedor.

### Como funciona el despliegue

1. Render construye la imagen Docker usando el `Dockerfile` raiz (multi-stage build con Maven + JRE)
2. El `docker-entrypoint.sh` arranca los 6 servicios como procesos background
3. Cada JVM usa `-Xmx48m -Xms24m` para maximizar el uso de memoria
4. El Gateway (puerto 8080) es el unico puerto expuesto externamente

---

## Rutas principales del Gateway

| Ruta | Servicio destino | Metodo |
|---|---|---|
| `/api/users/register` | user-service | POST |
| `/api/users/login` | user-service | POST |
| `/api/users/{id}` | user-service | GET |
| `/api/books` | book-service | GET, POST |
| `/api/books/{id}` | book-service | GET, PUT, DELETE |
| `/api/reviews` | review-service | GET, POST |
| `/api/reviews/user/{userId}` | review-service | GET |
| `/api/lists` | reading-list-service | GET, POST |
| `/api/lists/user/{userId}` | reading-list-service | GET |
| `/api/social/feed/{userId}` | social-service | GET |
| `/api/social/follow` | social-service | POST |
| `/api/recommendations` | recommendation-service | GET |
| `/api/notifications` | notification-service | GET |
| `/api/search` | search-service | GET |
| `/api/stats/{userId}` | stats-service | GET |
| `/api/audit` | audit-service | GET, POST |

---

## Usuarios de prueba

El sistema no tiene roles ni usuarios predefinidos. Para probar, registra un usuario via API:

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan Perez","email":"juan@test.com","password":"12345678"}'
```

Luego inicia sesion para obtener un token JWT:

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"juan@test.com","password":"12345678"}'
```

Usa el token en peticiones autenticadas:

```bash
curl http://localhost:8080/api/books \
  -H "Authorization: Bearer <token>"
```

---

## Estructura del proyecto

```
tintero-social/
├── eureka-server/            # Servidor de descubrimiento Eureka
├── user-service/             # Gestion de usuarios y autenticacion
├── book-service/             # Catalogo de libros
├── review-service/           # Resenas y valoraciones
├── reading-list-service/     # Listas de lectura personales
├── social-service/           # Seguimiento y actividad social
├── recommendation-service/   # Motor de recomendaciones
├── notification-service/     # Notificaciones a usuarios
├── search-service/           # Busqueda de libros y usuarios
├── stats-service/            # Estadisticas de lectura
├── audit-service/            # Registro de auditoria
├── gateway-service/          # API Gateway (Spring Cloud Gateway)
├── compose.yml               # Orquestacion Docker
├── Dockerfile                # Build para Render (single container)
├── docker-entrypoint.sh      # Entrypoint para Render
├── .dockerignore             # Exclusiones para Docker
├── .env.example              # Variables de entorno de referencia
├── pom.xml                   # POM raiz (multi-modulo)
├── mvnw                      # Maven Wrapper (Linux/macOS)
└── mvnw.cmd                  # Maven Wrapper (Windows)
```

---

## Equipo

Proyecto academico desarrollado para el curso **DSY1103** de [DuocUC](https://www.duoc.cl).

- **Javier Escalona Padilla** — Desarrollo de microservicios, API Gateway, Docker, pruebas unitarias
- **Eloy Contreras** — Desarrollo de microservicios, documentacion Swagger, configuracion YAML

---

## Gestion del proyecto

El seguimiento de tareas se realiza via **GitHub Issues** en el repositorio: [https://github.com/Javi-EP/tintero-social/issues](https://github.com/Javi-EP/tintero-social/issues)

---

## Licencia

Este proyecto fue desarrollado con fines academicos.
