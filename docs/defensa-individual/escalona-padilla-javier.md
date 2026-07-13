# Defensa Tecnica Individual — Javier Escalona Padilla

> DSY1103 Desarrollo FullStack 1 · DuocUC · 2025

---

## 1. Identificacion

| Campo | Valor |
|-------|-------|
| Nombre | Javier Escalona Padilla |
| Rol en el equipo | Desarrollador Backend |
| Repositorio | [https://github.com/Javi-EP/tintero-social](https://github.com/Javi-EP/tintero-social) |
| Commits propios | 84 de 133 totales (63%) |

---

## 2. Funcionalidades y modulos en los que participe

### Creacion desde cero
- **Estructura del proyecto**: POM raiz multi-modulo, configuracion Maven wrapper, .gitignore global
- **user-service**: Modelo, DTOs, repository, service, controller, SecurityConfig, JwtUtil, JwtAuthFilter, pruebas unitarias
- **book-service**: Modelo, DTOs, repository, service, controller, OpenApiConfig, pruebas unitarias
- **reading-list-service**: Modelo (ReadingList, ReadingListItem, ListType enum), DTOs, repository, service, controller, clientes inter-servicio (UserClient, BookClient), pruebas unitarias
- **gateway-service**: Creacion completa del API Gateway con Spring Cloud Gateway
- **eureka-server**: Modulo completo de Netflix Eureka con @EnableEurekaServer
- **audit-service**: Modelo (AuditEvent, EventType enum), DTOs, mapper, repository, service, controller, pruebas unitarias

### Configuracion e infraestructura
- **Docker**: Dockerfile multi-stage (Maven a JRE), docker-entrypoint.sh, compose.yml con 22 contenedores
- **Despliegue Render**: Profile neon, optimizacion de memoria (-Xmx48m), ajuste de docker-entrypoint.sh para 6 servicios
- **Flyway**: 10 scripts V1__create_*.sql, configuracion en profiles prod/dev/neon
- **Eureka**: Cliente en 11 servicios, rutas lb:// en Gateway, configuracion en 3 profiles
- **Gateway filters**: StripPrefix=1 y AddRequestHeader en 30 rutas (10 servicios x 3 profiles)

### Documentacion tecnica
- **README.md**: Reescritura completa con 20 secciones
- **docs/matriz-requerimientos.md**: 54 RF + 10 RNF
- **docs/plan-cierre-feedback.md**: Feedback y correcciones
- **docs/pruebas-rest.http**: 71 requests de prueba
- **docs/levantamiento-requerimientos-actualizado.md**: Contraste inicio vs cierre
- **.env.example**: Variables de entorno documentadas

---

## 3. Commits propios mas relevantes

### Fase 1 — Estructura y modelos
| Hash | Mensaje |
|------|---------|
| `34f55ee` | feat: configuracion inicial del proyecto, pom.xml raiz multi modulo, book-service y user-service |
| `1699494` | feat: estructura basica del model User |
| `4ee8c0b` | feat: modelo (entidad JPA) + dependencia de validacion basica |
| `0e5b19e` | Creacion de estructura de carpetas |

### Fase 2 — User Service completo
| Hash | Mensaje |
|------|---------|
| `6e1c147` | feat: JwtUtil para el hash, validacion de registro, login y request |
| `f762e97` | feat: creacion de JwtAuthFilter para hacer GET request con bearer token |
| `336ad42` | feat: estructura security config y userService |
| `cadf972` | feat: manejo de excepciones en user-service |
| `ab2bca0` | feat: logs en user-service con Slf4j |

### Fase 3 — Microservicios de negocio
| Hash | Mensaje |
|------|---------|
| `c25a61f` | feat: estructura inicial de ReadingListService |
| `bb44afa` | feat: estructura inicial de ReadingListController |
| `c013817` | feat: Cambio de WebClient a RestClient |
| `0a008b0` | feat: estructura inicial de modulo social-service |
| `9ae9069` | feat: estructura inicial de notification-service |
| `b9cfc4a` | feat: creacion de modulo recommendation-service |
| `4e471e6` | feat: creacion de modulo search-service |
| `eed70d8` | feat: estructura inicial stats-service |
| `dc4ae95` | feat: estructura inicial audit-service |

### Fase 4 — Infraestructura y pruebas
| Hash | Mensaje |
|------|---------|
| `c1b1702` | Dockerizacion de user-service, book-service, review-service, reading-list-service y social-service |
| `feb0267` | Creacion de gateway-service (API Gateway) |
| `7d59610` | Ajuste de pruebas unitarias para los 10 microservicios |
| `2b1afa4` | Creacion de mappers para Unit Test de book-service, review-service, reading-list-service y social-service |

### Fase 5 — Deploy y correcciones
| Hash | Mensaje |
|------|---------|
| `173206e` | Spring Boot 4.0.6 a 3.4.0, Dockerfile y compose creados |
| `09c9cd3` | Perfil PostgreSQL, Gateway con rutas, Build multi-stage |
| `5d650a4` | docker-entrypoint.sh con 6 servicios esenciales, limite de memoria |
| `78d90ae` | StripPrefix=1 en los 3 archivos de configuracion del Gateway |

### Fase 6 — Documentacion y cierre
| Hash | Mensaje |
|------|---------|
| `96f6e22` | Agregado Eureka-Server y modificacion de archivos de configuracion |
| `cf1a7d5` | Scripts de migracion SQL y habilitacion de flyway |
| `3f1964b` | Actualizacion de README con la documentacion solicitada |
| `33fe17d` | HTTP de pruebas REST |

**Enlace completo**: [https://github.com/Javi-EP/tintero-social/commits/main?author=Javi-EP](https://github.com/Javi-EP/tintero-social/commits/main?author=Javi-EP)

---

## 4. Tareas  asociadas a mi trabajo

El seguimiento se realiza via **GitHub**: [https://github.com/Javi-EP/tintero-social](https://github.com/Javi-EP/tintero-social/issues)

Tareas principales atendidas:
- Creacion de estructura base del proyecto y todos los microservicios
- Implementacion completa de user-service (JWT, security, CRUD)
- Implementacion de book-service y reading-list-service
- Creacion del API Gateway con Spring Cloud Gateway
- Configuracion de Docker y despliegue en Render
- Implementacion de Eureka Server
- Flyway y migraciones SQL
- Pruebas unitarias para los 10 servicios
- Documentacion tecnica completa (README, matriz, plan de cierre, .http)

---

## 5. Feedback corregido personalmente

| Feedback / Pendiente | Correccion aplicada |
|---------------------|---------------------|
| Gateway sin filtros (solo predicado de ruta) | Anadi StripPrefix=1 y AddRequestHeader=X-Forwarded-Prefix en las 30 rutas del Gateway (10 servicios x 3 profiles) |
| Sin servidor de descubrimiento | Cree el modulo eureka-server con @EnableEurekaServer y configure cliente en los 11 servicios |
| Sin migraciones SQL | Agregue Flyway con 10 scripts V1__create_*.sql y lo configure en profile prod |
| Sin .env.example | Cree .env.example con 8 variables documentadas y agrege .env a .gitignore |
| Documentacion incompleta | Reescribi el README con 20 secciones y cree matriz de requerimientos |
| Logging faltante | Agrege @Slf4j a todos los services y exception handlers que no lo tenian |
| ControllerAdvice faltante | Cree GlobalExceptionHandler.java en los servicios que no lo implementaban |
| Spring Boot 4.0.6 incompatible con Gateway | Regrese a Spring Boot 3.4.0 y exclui spring-boot-starter-web del HATEOAS heredado |

---

## 6. Archivos principales que modifique

### Infraestructura
- `pom.xml` (raiz) — Agregue modulos, Spring Cloud BOM, dependencias
- `compose.yml` — 22 contenedores, profiles, eureka-server
- `Dockerfile` — Build multi-stage Maven a JRE
- `docker-entrypoint.sh` — Arranque de 6 servicios con limite de memoria
- `.env.example` — Variables de entorno documentadas
- `.gitignore` — Exclusion de .env

### Gateway
- `gateway-service/src/main/resources/application-dev.yaml` — 10 rutas lb:// con StripPrefix + AddRequestHeader
- `gateway-service/src/main/resources/application-prod.yaml` — 10 rutas lb:// + config Eureka
- `gateway-service/src/main/resources/application-neon.yaml` — 10 rutas URLs directas

### User Service
- `user-service/src/main/java/.../model/User.java` — Entidad JPA
- `user-service/src/main/java/.../security/JwtUtil.java` — Generacion y validacion JWT
- `user-service/src/main/java/.../security/JwtAuthFilter.java` — Filtro de autenticacion
- `user-service/src/main/java/.../config/SecurityConfig.java` — Cadena de seguridad
- `user-service/src/main/java/.../service/UserService.java` — Logica de negocio
- `user-service/src/main/java/.../controller/UserController.java` — 7 endpoints
- `user-service/src/main/java/.../GlobalExceptionHandler.java` — Manejo de excepciones

### Book Service
- `book-service/src/main/java/.../model/Book.java` — Entidad JPA
- `book-service/src/main/java/.../controller/BookController.java` — 5 endpoints

### Reading List Service
- `reading-list-service/src/main/java/.../model/ReadingList.java` — Entidad JPA
- `reading-list-service/src/main/java/.../model/ReadingListItem.java` — Entidad JPA
- `reading-list-service/src/main/java/.../model/ListType.java` — Enum (WANT_TO_READ, READING, READ)
- `reading-list-service/src/main/java/.../client/UserClient.java` — Comunicacion con user-service
- `reading-list-service/src/main/java/.../client/BookClient.java` — Comunicacion con book-service

### Documentacion
- `docs/matriz-requerimientos.md` — 54 RF + 10 RNF
- `docs/plan-cierre-feedback.md` — Feedback y correcciones
- `docs/pruebas-rest.http` — 71 requests de prueba
- `docs/levantamiento-requerimientos-actualizado.md` — Contraste inicio vs cierre
- `README.md` — Documentacion completa del proyecto

---

## 7. Endpoints y flujos asociados a mi aporte

### User Service (7 endpoints) — Creado completamente
```
POST /api/users/register    — Registro de usuario (BCrypt hash)
POST /api/users/login       — Autenticacion y retorno de JWT
GET  /api/users/validate    — Validacion de token
GET  /api/users             — Listar todos
GET  /api/users/{id}        — Obtener por ID
PUT  /api/users/{id}        — Actualizar perfil
DELETE /api/users/{id}      — Eliminar cuenta
```

### Book Service (5 endpoints) — Creado completamente
```
GET  /api/books             — Listar con filtro por titulo
GET  /api/books/{id}        — Obtener por ID
POST /api/books             — Crear libro (ISBN unico)
PUT  /api/books/{id}        — Actualizar
DELETE /api/books/{id}      — Eliminar
```

### Reading List Service (7 endpoints) — Creado completamente
```
GET  /api/lists/user/{userId}            — Listas de un usuario
POST /api/lists                          — Crear lista
GET  /api/lists/{id}                     — Obtener por ID
POST /api/lists/{id}/books               — Agregar libro
PUT  /api/lists/{listId}/books/{bookId}/progress — Actualizar progreso
DELETE /api/lists/{listId}/books/{bookId} — Remover libro
DELETE /api/lists/{id}                   — Eliminar lista
```

### Gateway (rutas configuradas)
```
/api/users/**          -> lb://user-service     (8081)
/api/books/**          -> lb://book-service     (8082)
/api/reviews/**        -> lb://review-service   (8083)
/api/lists/**          -> lb://reading-list-service (8084)
/api/social/**         -> lb://social-service   (8085)
/api/recommendations/** -> lb://recommendation-service (8086)
/api/notifications/**  -> lb://notification-service (8087)
/api/search/**         -> lb://search-service   (8088)
/api/stats/**          -> lb://stats-service    (8089)
/api/audit/**          -> lb://audit-service    (8090)
```

---

## 8. Pruebas unitarias asociadas a mi aporte

### user-service (UserServiceTest + JwtUtilTest)
- `register_shouldSaveUser_whenEmailIsNotTaken`
- `register_shouldThrowException_whenEmailIsDuplicate`
- `login_shouldReturnToken_whenCredentialsAreValid`
- `login_shouldThrowException_whenCredentialsAreInvalid`
- `findAll_shouldReturnList_whenUsersExist`
- `findById_shouldReturnUser_whenExists`
- `findById_shouldThrowException_whenNotExists`
- `update_shouldUpdateNameAndBio_whenUserExists`
- `delete_shouldRemoveUser_whenExists`
- `generateAndValidateToken_shouldReturnTrue_whenTokenIsValid`

### book-service (BookServiceTest)
- `listAll_shouldReturnAllBooks_whenBooksExist`
- `findById_shouldReturnBook_whenExists`
- `save_shouldCreateBook_whenIsbnIsNotTaken`
- `save_shouldThrowException_whenIsbnIsDuplicate`
- `update_shouldModifyAllFields_whenBookExists`
- `delete_shouldRemoveBook_whenExists`

### reading-list-service (ReadingListServiceTest)
- `createList_shouldSave_whenUserExists`
- `getListsByUser_shouldReturnLists`
- `getListById_shouldReturnList_whenExists`
- `deleteList_shouldRemove_whenExists`
- `addBook_shouldAdd_whenBookNotInList`
- `addBook_shouldThrowException_whenBookAlreadyInList`
- `updateProgress_shouldSetProgress`
- `removeBook_shouldDelete_whenItemExists`

### Pruebas REST (docs/pruebas-rest.http)
- 71 requests que cubren: casos exitosos, datos invalidos, recursos inexistentes, permisos, comunicacion entre servicios, Gateway

---

## 9. Regla de negocio que domino

### Validacion de email unico en registro

El user-service valida que no existan dos usuarios con el mismo email antes de crear uno nuevo. El flujo es:

1. El `UserController` recibe `POST /api/users/register` con un `UserRequestDTO`
2. El `UserService.register()` verifica si el email ya existe via `userRepository.findByEmail()`
3. Si existe, lanza una excepcion personalizada que el `GlobalExceptionHandler` convierte en HTTP 409
4. Si no existe, hashea la contrasena con `BCryptPasswordEncoder.encode()` y guarda el usuario
5. Retorna el usuario creado como `UserResponseDTO`

**Archivos clave**: `UserService.java:register()`, `UserRepository.java:findByEmail()`, `GlobalExceptionHandler.java`

Esta regla es fundamental porque:
- Previene cuentas duplicadas
- Protege la integridad de la BD (constraint UNIQUE en email)
- La contrasena nunca se almacena en texto plano

---

## 10. Relacion de base de datos que domino

### ReadingList -> ReadingListItem (One-to-Many)

La entidad `ReadingList` tiene una relacion One-to-Many con `ReadingListItem`:

```
ReadingList (reading_lists)
+-- id (PK)
+-- userId (NOT NULL)
+-- name (NOT NULL)
+-- type (enum: WANT_TO_READ, READING, READ)
+-- isPrivate (default false)
+-- createdAt
+-- items: List<ReadingListItem>  <-- @OneToMany(cascade=ALL, orphanRemoval=true)

ReadingListItem (reading_list_items)
+-- id (PK)
+-- bookId (NOT NULL)
+-- progress (0-100%, default 0)
+-- addedAt
+-- finishedAt (nullable)
+-- readingList: ReadingList      <-- @ManyToOne(LAZY)
```

**Cascade ALL** significa que al crear/eliminar una lista, sus items se crean/eliminan automaticamente. **OrphanRemoval** asegura que al quitar un item de la lista, se elimine de la BD. Esta relacion es clave para el funcionamiento de listas de lectura personalizadas.

---

## 11. Comunicacion entre servicios que domino

### Reading List Service -> User Service (RestClient)

El `reading-list-service` consulta al `user-service` para validar que un usuario exista antes de crear una lista:

```java
// ReadingListService.java
public ReadingListDTO createList(ReadingListDTO dto) {
    // Valida que el usuario exista en user-service
    UserDTO user = userClient.getUserById(dto.getUserId());
    if (user == null) {
        throw new RuntimeException("Usuario no encontrado");
    }
    // Crea la lista...
}
```

```java
// UserClient.java (RestClient)
public UserDTO getUserById(Long id) {
    return restTemplate.getForObject("/api/users/" + id, UserDTO.class);
}
```

**Flujo**: ReadingListService -> UserClient (RestClient) -> Gateway -> user-service -> Respuesta JSON

Esta comunicacion usa **Spring RestClient** (nuevo en Spring 6), que es sincrono y mas simple que WebClient. El Gateway resuelve `lb://user-service` via Eureka para encontrar la IP y puerto correctos.

---

## 12. Dificultad tecnica personal y como la resolvi

### Despliegue en Render con memoria insuficiente

**Problema**: Render (plan gratuito) ofrece 512 MB de memoria. Mi plan original era desplegar los 6 servicios esenciales (user, book, review, reading-list, social, gateway) en un solo contenedor Docker. Cada JVM consume entre 64-128 MB, y con 6 JVMs se superaba el limite de memoria.

**Intentos fallidos**:
1. Primero intente con todos los servicios: 6 JVMs + MySQL = ~800 MB. Error inmediato.
2. Reduje a 6 servicios esenciales pero sin optimizar memoria: ~600 MB. Error a los minutos.

**Solucion final**:
1. Compile los 11 JARs en un Dockerfile multi-stage (Maven build a JRE)
2. El `docker-entrypoint.sh` arranca solo 6 servicios como procesos background
3. Cada JVM usa `-Xmx48m -Xms24m` para maximizar memoria
4. Configure el profile `neon` con PostgreSQL (Neon) y Eureka desactivado
5. El Gateway usa URLs directas (localhost:808X) ya que todos estan en el mismo contenedor

**Resultado**: El deploy funciona pero a veces falla por memoria. Se identifico que requiere un plan de pago de Render o separar cada servicio en su propio Web Service.

---

## 13. Checklist personal de evidencia entregada

- [x] Codigo fuente completo en GitHub (12 modulos Maven)
- [x] Todos los microservicios funcionando (10 de negocio + gateway + eureka)
- [x] Pruebas unitarias: 132 metodos @Test en 26 archivos
- [x] Docker Compose funcional (22 contenedores)
- [x] Despliegue en Render configurado (profile neon)
- [x] README.md completo con 20 secciones
- [x] docs/matriz-requerimientos.md (54 RF + 10 RNF)
- [x] docs/plan-cierre-feedback.md (feedback y correcciones)
- [x] docs/pruebas-rest.http (71 requests de prueba)
- [x] docs/levantamiento-requerimientos-actualizado.md (contraste)
- [x] .env.example con variables documentadas
- [x] .gitignore excluye .env
- [x] Eureka Server funcionando
- [x] Flyway migrations configuradas
- [x] Gateway con StripPrefix y AddRequestHeader
- [x] Swagger/OpenAPI en 10 servicios
- [x] Presentacion PPTX (20 slides)
- [x] Defensa individual (este documento)
