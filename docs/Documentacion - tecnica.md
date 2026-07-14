# Documentación Técnica — Tintero Social

> Plataforma Social de Lectura  
> DSY1103 Desarrollo FullStack 1 · DuocUC · 2025

---

## 1. Arquitectura general

Tintero Social está desarrollado con **arquitectura de microservicios** usando Spring Boot 4. El sistema se compone de 10 servicios independientes, cada uno con su propia base de datos, comunicados entre sí únicamente a través de APIs REST.

```
[ Cliente Web / App ]
         ↓ HTTP/REST
   [ api-gateway :8080 ]
    Valida JWT · Enruta
         ↓
┌─────────────────────────────────────────────────────┐
│                  Microservicios                      │
│                                                     │
│  user-service      :8081   db_users                 │
│  book-service      :8082   db_books                 │
│  review-service    :8083   db_reviews               │
│  reading-list      :8084   db_reading_lists         │
│  social-service    :8085   db_social                │
│  recommendation    :8086   db_recommendations       │
│  notification      :8087   db_notifications         │
│  search-service    :8088   db_search                │
│  stats-service     :8089   db_stats                 │
│  audit-service     :8090   db_audit                 │
└─────────────────────────────────────────────────────┘
```

Cada microservicio sigue la misma estructura de capas interna:

```
Controller → Service → Repository → Entity (Base de datos)
     ↕            ↕
    DTO         Validaciones y reglas de negocio
```

---

## 2. Diagrama de microservicios

| Servicio | Puerto | Base de datos | Responsabilidad |
|---|---|---|---|
| api-gateway | 8080 | N/A | Punto de entrada único, JWT, enrutamiento |
| user-service | 8081 | db_users | Registro, autenticación y perfiles |
| book-service | 8082 | db_books | Catálogo de libros |
| review-service | 8083 | db_reviews | Reseñas y calificaciones |
| reading-list-service | 8084 | db_reading_lists | Listas de lectura personales |
| social-service | 8085 | db_social | Seguimiento y feed de actividad |
| recommendation-service | 8086 | db_recommendations | Recomendaciones personalizadas |
| notification-service | 8087 | db_notifications | Notificaciones internas |
| search-service | 8088 | db_search | Búsqueda de libros y usuarios |
| stats-service | 8089 | db_stats | Estadísticas de lectura |
| audit-service | 8090 | db_audit | Auditoría de eventos del sistema |

---

## 3. Responsabilidades por servicio

### user-service
Gestiona el ciclo de vida de los usuarios: registro, autenticación con JWT y gestión de perfiles. Es consultado por la mayoría de los otros servicios para validar que un usuario existe antes de realizar operaciones.

### book-service
Gestiona el catálogo completo de libros. Es la fuente de verdad para los metadatos de libros. Usa HATEOAS para enriquecer las respuestas con enlaces de navegación.

### review-service
Gestiona reseñas y calificaciones. Aplica reglas importantes: una reseña por usuario por libro, rating entre 1 y 5, y solo el autor puede editar o eliminar su reseña.

### reading-list-service
Gestiona las listas de lectura personales con tres estados: WANT_TO_READ, READING, READ. Registra el progreso de lectura y la fecha de finalización. Usa HATEOAS.

### social-service
Gestiona las relaciones de seguimiento entre usuarios y mantiene el feed de actividad. Notifica al notification-service cuando se registra un nuevo seguidor.

### recommendation-service
Genera recomendaciones personalizadas basadas en historial y géneros favoritos. Implementa borrado lógico para recomendaciones descartadas.

### notification-service
Gestiona notificaciones internas. Recibe eventos de otros servicios y los convierte en notificaciones para los usuarios.

### search-service
Mantiene un índice propio de libros y usuarios para búsquedas rápidas sin depender de otros servicios en tiempo real.

### stats-service
Calcula estadísticas personales (libros leídos, género favorito, racha) y métricas globales de la plataforma.

### audit-service
Registra eventos de auditoría del sistema para trazabilidad de operaciones importantes.

---

## 4. Modelo de datos

### user-service
```
users
  id (PK), nombre, email, password_hash, bio, avatar_url, fecha_registro

roles
  id (PK), nombre
```

### book-service
```
books
  id (PK), titulo, autor, isbn (UNIQUE), genero, sinopsis, portada_url, anio_publicacion

genres
  id (PK), nombre
```

### review-service
```
reviews
  id (PK), user_id, book_id, rating, title, content, created_at, updated_at
  UNIQUE(user_id, book_id)

votes
  id (PK), user_id, review_id, type
  UNIQUE(user_id, review_id)
```

### reading-list-service
```
reading_lists
  id (PK), user_id, name, type (ENUM: WANT_TO_READ/READING/READ), is_private, created_at

reading_list_items
  id (PK), reading_list_id (FK), book_id, progress, added_at, finished_at
```

### social-service
```
follows
  id (PK), follower_id, followed_id, fecha_inicio
  UNIQUE(follower_id, followed_id)

feed
  id (PK), user_id, evento_tipo, referencia_id, fecha
```

### recommendation-service
```
recommendations
  id (PK), user_id, book_id, score, reason, dismissed (BOOL), created_at

genre_preferences
  id (PK), user_id, genre_id, weight
  UNIQUE(user_id, genre_id)
```

### notification-service
```
notifications
  id (PK), user_id, sender_id, type, message, is_read (BOOL), created_at
```

### search-service
```
book_index
  id (PK), book_id, title, author, genre, tags, rating

user_index
  id (PK), user_id, name, bio
```

### stats-service
```
user_stats
  id (PK), user_id, libros_leidos, paginas_leidas, genero_favorito, streak

global_metrics
  id (PK), fecha, total_usuarios, total_resenas, libro_mas_leido
```

### audit-service
```
audit_event
  id (PK), user_id, sender_id, event_type (ENUM), description, resource_id, resource_type, created_at
```

---

## 5. Relaciones principales

Los microservicios no comparten bases de datos. Las relaciones entre entidades de diferentes servicios se mantienen a través de IDs referenciados:

- `review-service` almacena `userId` y `bookId` como campos Long, sin FK entre bases de datos.
- `reading-list-service` almacena `userId` y `bookId` como campos Long.
- `recommendation-service` almacena `userId` y `bookId` como campos Long.
- `notification-service` almacena `userId` y `senderId` como campos Long.
- `audit-service` almacena `userId` y `resourceId` como campos Long.

---

## 6. Perfiles de configuración

Cada servicio soporta dos perfiles de configuración:

| Perfil | Base de datos | Uso |
|---|---|---|
| `dev` | H2 en memoria | Desarrollo y pruebas locales |
| `prod` | MySQL | Despliegue en producción |

Para activar un perfil, se configura en `application.yaml`:

```yaml
spring:
  profiles:
    active: dev  # o prod
```

---

## 7. Variables de entorno

Para el perfil `prod`, cada servicio requiere las siguientes variables de entorno:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_HOST` | Host de la base de datos MySQL | `localhost` |
| `DB_PORT` | Puerto de MySQL | `3306` |
| `DB_NAME` | Nombre de la base de datos | `db_reviews` |
| `DB_USERNAME` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | `secret` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | `mi-clave-secreta-256bits` |

Estas variables se definen en un archivo `.env` en la raíz del proyecto o se inyectan directamente en el entorno de ejecución.

### Ejemplo de archivo `.env`
```env
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=secret
JWT_SECRET=tintero-social-jwt-secret-key-2025
```

---

## 8. Seguridad

- Las contraseñas se almacenan con **BCrypt** (RNF-06). Nunca en texto plano.
- Toda petición a endpoints protegidos requiere un **token JWT válido** en la cabecera `Authorization: Bearer <token>` (RNF-07).
- Peticiones sin token o con token inválido reciben **HTTP 401 Unauthorized**.
- El `api-gateway` intercepta y valida el token antes de enrutar la petición al microservicio correspondiente.
- Los endpoints de administración requieren el rol `ADMIN`.

---

## 9. Comunicación entre servicios

La comunicación entre microservicios se realiza de forma **síncrona** mediante **WebClient** o **Feign Client** de Spring Boot (RNF-05). Ningún servicio accede directamente a la base de datos de otro.

Ejemplos de comunicación:
- `review-service` → `user-service` para validar que el usuario existe.
- `review-service` → `book-service` para validar que el libro existe.
- `social-service` → `notification-service` al registrar un nuevo seguidor.
- `review-service` → `notification-service` al recibir un voto.
- `search-service` → `book-service` y `user-service` para construir sus índices.

---

## 10. Manejo de errores

Todos los servicios utilizan `ResponseStatusException` para retornar errores HTTP descriptivos:

| Código HTTP | Cuándo se usa |
|---|---|
| 200 OK | Petición exitosa |
| 201 Created | Recurso creado correctamente |
| 204 No Content | Operación exitosa sin contenido (DELETE) |
| 400 Bad Request | Datos de entrada inválidos o faltantes (RNF-02) |
| 401 Unauthorized | Token JWT ausente o inválido (RNF-07) |
| 403 Forbidden | El usuario no tiene permiso para la operación |
| 404 Not Found | El recurso solicitado no existe |
| 409 Conflict | El recurso ya existe (email duplicado, ISBN duplicado, etc.) |

---

## 11. Logs

Spring Boot genera logs automáticamente por consola. Para producción se recomienda configurar un nivel de log adecuado en `application.yaml`:

```yaml
logging:
  level:
    root: INFO
    cl.javiep: DEBUG
```

El audit-service registra eventos de auditoría en la base de datos para trazabilidad de operaciones críticas (creación de usuarios, reseñas, seguimientos, etc.).

---

## 12. Pruebas

Cada servicio incluye pruebas unitarias en `src/test/java`. Para ejecutar las pruebas de un servicio:

**Linux/macOS:**
```bash
./mvnw test
```

**Windows:**
```cmd
mvnw.cmd test
```

---

## 13. Estructura del repositorio

```
tintero-social/
├── api-gateway/
│   ├── src/main/java/cl/javiep/apigateway/
│   └── pom.xml
├── audit-service/
│   ├── src/main/java/cl/javiep/auditservice/
│   │   ├── config/          # OpenApiConfig
│   │   ├── controller/      # AuditController
│   │   ├── dto/             # AuditEventRequestDTO, AuditEventResponseDTO
│   │   ├── model/           # AuditEvent, EventType
│   │   ├── repository/      # AuditRepository
│   │   └── service/         # AuditService
│   ├── src/main/resources/
│   │   ├── application.yaml
│   │   ├── application-dev.yaml
│   │   └── application-prod.yaml
│   └── pom.xml
├── book-service/
│   └── ... (misma estructura)
├── notification-service/
│   └── ... (misma estructura)
├── reading-list-service/
│   └── ... (misma estructura)
├── recommendation-service/
│   └── ... (misma estructura)
├── review-service/
│   └── ... (misma estructura)
├── search-service/
│   └── ... (misma estructura)
├── social-service/
│   └── ... (misma estructura)
├── stats-service/
│   └── ... (misma estructura)
├── user-service/
│   └── ... (misma estructura)
├── docs/
│   ├── documentacion-funcional.md
│   └── documentacion-tecnica.md
├── docker-compose.yml
├── .env.example
└── pom.xml                  # POM padre del proyecto
```

---

## 14. Ejecución desde cero

Esta sección permite que cualquier persona clone el repositorio y ejecute el sistema sin necesidad de IntelliJ IDEA, VS Code ni ninguna configuración guardada en el computador del equipo.

### 14.1 Requisitos previos

Instalar las siguientes herramientas antes de continuar:

| Herramienta | Versión mínima | Descarga |
|---|---|---|
| Java JDK | 21 o superior | https://adoptium.net |
| Docker Desktop | 24.x o superior | https://www.docker.com/products/docker-desktop |
| Docker Compose | Incluido en Docker Desktop | — |
| Git | 2.x | https://git-scm.com |

Verificar instalación:
```bash
java -version
docker --version
docker compose version
git --version
```

### 14.2 Clonar el repositorio

```bash
git clone https://github.com/Javi-EP/tintero-social.git
cd tintero-social
```

### 14.3 Configurar el archivo `.env`

Copiar el archivo de ejemplo y completar las variables:

**Linux/macOS:**
```bash
cp .env.example .env
```

**Windows:**
```cmd
copy .env.example .env
```

Editar el archivo `.env` con los valores correctos:

```env
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=secret
JWT_SECRET=tintero-social-jwt-secret-key-2025
```

### 14.4 Levantar bases de datos con Docker Compose

```bash
docker compose up -d
```

Verificar que los contenedores están corriendo:
```bash
docker compose ps
```

Ver logs de las bases de datos:
```bash
docker compose logs -f
```

Para detener los contenedores:
```bash
docker compose down
```

### 14.5 Ejecutar migraciones

Las migraciones se ejecutan automáticamente al iniciar cada servicio con Spring Boot (configuración `ddl-auto: update` en producción o `create-drop` en desarrollo).

No se requiere ejecutar migraciones manuales.

### 14.6 Orden exacto de arranque de servicios

Los servicios deben iniciarse en este orden para respetar las dependencias:

1. `user-service` (otros servicios lo consultan para validar usuarios)
2. `book-service` (review-service y reading-list-service lo consultan)
3. `review-service`
4. `reading-list-service`
5. `social-service`
6. `recommendation-service`
7. `notification-service`
8. `search-service`
9. `stats-service`
10. `audit-service`
11. `api-gateway` (último, ya que enruta a todos los demás)

### 14.7 Comandos para iniciar cada servicio

Desde la raíz del proyecto, entrar a la carpeta del servicio y ejecutar:

**Linux/macOS:**
```bash
# Ejemplo para user-service
cd user-service
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Volver a la raíz y repetir para cada servicio
cd ..
cd book-service
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

**Windows:**
```cmd
cd user-service
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod

cd ..
cd book-service
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

Para desarrollo local con H2 (sin Docker):
```bash
# Linux/macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### 14.8 Comandos para correr pruebas

```bash
# Linux/macOS — desde la carpeta de cada servicio
./mvnw test

# Windows
mvnw.cmd test

# Desde la raíz — correr pruebas de todos los módulos
# Linux/macOS
./mvnw test --projects user-service,book-service,review-service

# Windows
mvnw.cmd test --projects user-service,book-service,review-service
```

### 14.9 URLs de Swagger/OpenAPI por servicio

Una vez que los servicios están corriendo, acceder a la documentación interactiva en:

| Servicio | Swagger UI | API Docs JSON |
|---|---|---|
| api-gateway | http://localhost:8080/swagger-ui/index.html | http://localhost:8080/v3/api-docs |
| user-service | http://localhost:8081/swagger-ui/index.html | http://localhost:8081/v3/api-docs |
| book-service | http://localhost:8082/swagger-ui/index.html | http://localhost:8082/v3/api-docs |
| review-service | http://localhost:8083/swagger-ui/index.html | http://localhost:8083/v3/api-docs |
| reading-list-service | http://localhost:8084/swagger-ui/index.html | http://localhost:8084/v3/api-docs |
| social-service | http://localhost:8085/swagger-ui/index.html | http://localhost:8085/v3/api-docs |
| recommendation-service | http://localhost:8086/swagger-ui/index.html | http://localhost:8086/v3/api-docs |
| notification-service | http://localhost:8087/swagger-ui/index.html | http://localhost:8087/v3/api-docs |
| search-service | http://localhost:8088/swagger-ui/index.html | http://localhost:8088/v3/api-docs |
| stats-service | http://localhost:8089/swagger-ui/index.html | http://localhost:8089/v3/api-docs |
| audit-service | http://localhost:8090/swagger-ui/index.html | http://localhost:8090/v3/api-docs |

### 14.10 Verificar que el sistema quedó funcionando

Verificar el health check de cada servicio:

```bash
# Linux/macOS
curl http://localhost:8081/api/users/health
curl http://localhost:8082/api/books/health
curl http://localhost:8083/api/reviews/health
curl http://localhost:8084/api/lists/health
curl http://localhost:8086/api/recommendations/health
curl http://localhost:8087/api/notifications/health
curl http://localhost:8088/api/search/health
curl http://localhost:8090/api/audit/health

# Windows (PowerShell)
Invoke-WebRequest -Uri http://localhost:8083/api/reviews/health
```

Cada endpoint debe responder con HTTP 200 y el mensaje `<nombre-servicio> OK`.

También se puede verificar que Swagger carga correctamente abriendo cualquiera de las URLs de la sección 14.9 en el navegador.

---

## 15. Despliegue remoto

Para despliegue en servidor remoto o nube:

1. Configurar las variables de entorno en el servidor (no usar el archivo `.env` en producción directamente).
2. Usar `mvnw package -DskipTests` para generar el JAR de cada servicio.
3. Ejecutar cada JAR con `java -jar target/<nombre-servicio>-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`.
4. Asegurarse de que las bases de datos MySQL están accesibles desde el servidor.
5. Configurar un proxy reverso (nginx o similar) para exponer el `api-gateway` externamente.