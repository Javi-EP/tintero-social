# Levantamiento de Requerimientos Actualizado — Tintero Social

> Documento de contraste entre lo declarado al inicio del curso (Evaluacion Parcial 2) y lo efectivamente implementado al cierre del semestre.

---

## Resumen general

| Concepto | Inicio del curso | Cierre del semestre |
|----------|-----------------|---------------------|
| Microservicios planificados | 10 (sin eureka) | 11 + eureka-server |
| RFs planteados | 20 | 20 (18 iguales, 2 parciales) |
| RNFs planteados | 7 | 7 (4 iguales, 2 parciales, 1 eliminado) |
| Entidades JPA planificadas | ~18 | 15 |
| Endpoints planificados | ~50 | 57 (+ audit-service) |
| Pruebas unitarias | 0 | 132 metodos @Test |

---

## Requerimientos funcionales — Contraste RF por RF

| ID | Requerimiento original | Cambio realizado | Justificacion | Estado final | Evidencia en repositorio |
|----|----------------------|------------------|---------------|--------------|--------------------------|
| RF-01 | Registrar un nuevo usuario con nombre, correo electronico y contrasena encriptada | Sin cambios | Se mantuvo el flujo original | Implementado | `user-service/.../UserController.java` POST `/api/users/register`, `UserServiceTest.register_shouldSaveUser_whenEmailIsNotTaken` |
| RF-02 | Autenticar a un usuario existente y retornar un token JWT valido | Sin cambios | Se mantuvo el flujo original | Implementado | `user-service/.../UserController.java` POST `/api/users/login`, `UserServiceTest.login_shouldReturnToken_whenCredentialsAreValid` |
| RF-03 | Actualizar los datos del perfil propio (nombre, biografia, avatar) | Se elimino el campo `avatarUrl` | Durante la implementacion se determino que el manejo de imagenes de perfil requiere un servicio de almacenamiento (S3, etc.) que excedia el alcance del curso. Se mantuvo nombre y bio | Parcialmente implementado | `user-service/.../User.java` — campos: `name`, `email`, `passwordHash`, `bio`, `createdAt` (sin `avatarUrl`). `UserServiceTest.update_shouldUpdateNameAndBio_whenUserExists` |
| RF-04 | Eliminar la cuenta propia y todos los datos asociados | Sin cambios | Se mantuvo el flujo original | Implementado | `user-service/.../UserController.java` DELETE `/api/users/{id}`, `UserServiceTest.delete_shouldRemoveUser_whenExists` |
| RF-05 | Buscar libros por titulo, autor, genero o ISBN con paginacion | Se elimino la paginacion | La busqueda se implemento con `findBy*ContainingIgnoreCase` en el repositorio. Se priorizo la funcionalidad de busqueda global unificada sobre la paginacion. Los resultados se devuelven como lista completa | Parcialmente implementado | `search-service/.../SearchController.java` GET `/api/search?q=`, `/api/search/books?q=&genre=&rating=`. Sin soporte `Pageable`. `SearchServiceTest.globalSearch_shouldCombineBooksAndUsers` |
| RF-06 | Agregar un libro al catalogo con titulo, autor, ISBN, genero y sinopsis (admin) | Se elimino el control de roles | El sistema no implementa un modelo de roles. Cualquier usuario autenticado puede crear libros. Se mantuvo la validacion de ISBN unico y campos obligatorios | Parcialmente implementado | `book-service/.../BookController.java` POST `/api/books`. Sin endpoint de administrador. `BookServiceTest.save_shouldCreateBook_whenIsbnIsNotTaken` |
| RF-07 | Agregar un libro a una lista personal (por leer, leyendo, leido) | Sin cambios | Se mantuvo el flujo original con enum `ListType` (WANT_TO_READ, READING, READ) | Implementado | `reading-list-service/.../ReadingListController.java` POST `/api/lists/{id}/books`, `ReadingListServiceTest.addBook_shouldAdd_whenBookNotInList` |
| RF-08 | Actualizar el progreso de lectura de un libro en una lista | Sin cambios | Se mantuvo con campo `progress` (0-100%) | Implementado | `reading-list-service/.../ReadingListController.java` PUT `/api/lists/{listId}/books/{bookId}/progress`, `ReadingListServiceTest.updateProgress_shouldSetProgress` |
| RF-09 | Eliminar un libro de una lista de lectura personal | Sin cambios | Se mantuvo el flujo original | Implementado | `reading-list-service/.../ReadingListController.java` DELETE `/api/lists/{listId}/books/{bookId}`, `ReadingListServiceTest.removeBook_shouldDelete_whenItemExists` |
| RF-10 | Crear una resena con calificacion (1-5 estrellas) y texto sobre un libro leido | Sin cambios | Se mantuvo el flujo original | Implementado | `review-service/.../ReviewController.java` POST `/api/reviews`, `ReviewServiceTest.createReview_shouldSave_whenDataIsValid` |
| RF-11 | Editar o eliminar una resena propia | Sin cambios | Se valido autor via query param `?userId=` | Implementado | `review-service/.../ReviewController.java` PUT/DELETE `/api/reviews/{id}`, `ReviewServiceTest.updateReview_shouldModifyFields_whenOwner` |
| RF-12 | Votar una resena de otro usuario como util | Sin cambios | Se mantuvo con tabla `votes` | Implementado | `review-service/.../ReviewController.java` POST `/api/reviews/{id}/vote`, `ReviewServiceTest.voteReview_shouldSaveVote_whenValid` |
| RF-13 | Seguir a otro lector para ver su actividad en el feed personal | Sin cambios | Se mantuvo el flujo original | Implementado | `social-service/.../SocialController.java` POST `/api/social/follow`, `SocialServiceTest.follow_shouldSave_whenAllChecksPass` |
| RF-14 | Dejar de seguir a un lector previamente seguido | Sin cambios | Se mantuvo el flujo original | Implementado | `social-service/.../SocialController.java` DELETE `/api/social/follow`, `SocialServiceTest.unfollow_shouldRemove_whenFollowExists` |
| RF-15 | Visualizar el feed de actividad con las acciones recientes de usuarios seguidos | Se cambio la persistencia: feed computado en runtime via DTO | Se elimino la entidad `Feed` planificada. El feed se genera al momento consultando `ReviewClient` y `ReadingListClient` para agregar actividad de seguidos. Esto evita inconsistencias de datos y simplifica la sincronizacion | Implementado (enfoque diferente) | `social-service/.../SocialService.java` metodo `getFeed()` usa `FeedItemDTO`. Sin entidad JPA `Feed`. `SocialServiceTest.getFeed_shouldAggregateReviewsAndLists_whenFollowingUsers` |
| RF-16 | Recibir recomendaciones de libros basadas en generos favoritos e historial | Sin cambios | Se mantuvo con `GenrePreference` y scoring | Implementado | `recommendation-service/.../RecommendationController.java` GET `/api/recommendations/{userId}`, `RecommendationServiceTest.getRecommendations_shouldReturnList_whenExists` |
| RF-17 | Descartar una recomendacion para que no vuelva a aparecer | Sin cambios | Se mantuvo con campo `dismissed` | Implementado | `recommendation-service/.../RecommendationController.java` POST `/api/recommendations/{userId}/dismiss/{bookId}`, `RecommendationServiceTest.dismissRecommendation_shouldSetDismissed_whenFound` |
| RF-18 | Visualizar estadisticas personales (libros leidos, generos favoritos, racha de lectura) | Se cambio la entidad: `EstadisticaUsuario` → `StatSnapshot` | Se renombraron los campos para mayor claridad: `librosLeidos` → `booksRead`, `generoFavorito` → `favoriteGenre`. Se elimino `streak` (racha) por no tener datos suficientes para calcularla con precision | Implementado (enfoque diferente) | `stats-service/.../StatSnapshot.java` con campos `booksRead`, `booksReading`, `booksWantToRead`, `totalReviews`, `averageRating`, `favoriteGenre`. `StatsServiceTest.getUserStats_shouldCreateSnapshotWithComputedStats` |
| RF-19 | Recibir notificaciones internas al obtener un nuevo seguidor o votos en resenas | Sin cambios | Se mantuvo la entidad `Notification` con campos `userId`, `senderId`, `type`, `message`, `isRead` | Implementado | `notification-service/.../NotificationController.java` POST `/api/notifications`, `NotificationServiceTest.create_shouldSaveAndReturnDTO_whenSenderExists` |
| RF-20 | Marcar notificaciones como leidas de forma individual o masiva | Se elimino la marcacion masiva (`read-all`) | Se implemento solo la marcacion individual por simplicidad. La funcionalidad masiva se puede agregar en el futuro | Parcialmente implementado | `notification-service/.../NotificationController.java` PUT `/api/notifications/{id}/read`. Sin endpoint `read-all`. `NotificationServiceTest.markAsRead_shouldSetRead_whenExists` |

---

## Requerimientos no funcionales — Contraste RNF por RNF

| ID | Requerimiento original | Cambio realizado | Justificacion | Estado final | Evidencia en repositorio |
|----|----------------------|------------------|---------------|--------------|--------------------------|
| RNF-01 | Cada endpoint REST debe retornar HTTP en menos de 500ms bajo 50 peticiones concurrentes | Se elimino este requisito | No se realizaron benchmarks de rendimiento ni pruebas de carga. El sistema funciona en condiciones normales de desarrollo, pero no se验证o formalmente el cumplimiento de este umbral | Eliminado | Sin evidencia. No existen pruebas de rendimiento ni configuracion de stress testing |
| RNF-02 | Todos los datos de entrada deben ser validados; campos invalidos deben retornar HTTP 400 | Sin cambios | Se implemento con `@Valid`, `@NotBlank`, `@Column(nullable)` y `GlobalExceptionHandler` | Implementado | `user-service/.../GlobalExceptionHandler.java`, `book-service/.../Book.java` con `@NotBlank`. Todos los servicios tienen `GlobalExceptionHandler` |
| RNF-03 | Cada microservicio debe exponer un endpoint `/health` | Se implemento parcialmente | Solo 3 de 10 servicios incluyen `/health`: `review-service`, `search-service`, `recommendation-service`. Los demas servicios no lo implementaron por limitaciones de tiempo | Parcialmente implementado | `review-service/.../ReviewController.java` GET `/api/reviews/health`, `search-service/.../SearchController.java` GET `/api/search/health`, `recommendation-service/.../RecommendationController.java` GET `/api/recommendations/health` |
| RNF-04 | Cada microservicio debe operar con su propia base de datos independiente | Sin cambios | Se mantuvo Database per Service: 10 bases de datos separadas | Implementado | `compose.yml` define 10 servicios MySQL/PostgreSQL separados. Cada servicio tiene su propio `application-prod.yaml` con URL de BD independiente |
| RNF-05 | Comunicacion entre microservicios via WebClient o Feign Client | Se cambio tecnologia: RestClient en vez de Feign/WebClient | Se uso `org.springframework.web.client.RestClient` (nuevo en Spring 6) por ser mas simple y sincrono. WebClient (reactivo) no era necesario ya que los servicios son servlet-based. Feign requiere dependencia adicional | Implementado (tecnologia diferente) | `social-service/.../client/UserClient.java`, `reading-list-service/.../client/BookClient.java`, etc. Todos usan `RestClient.create(url)` o `RestClient.builder().baseUrl(baseUrl).build()` |
| RNF-06 | Contrasenas almacenadas con bcrypt, nunca en texto plano | Sin cambios | Se mantuvo `BCryptPasswordEncoder` | Implementado | `user-service/.../config/SecurityConfig.java` define `@Bean PasswordEncoder` con `BCryptPasswordEncoder` |
| RNF-07 | Toda peticion a endpoints protegidos debe incluir token JWT valido | Sin cambios | Se implemento `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter` | Implementado | `user-service/.../security/JwtAuthFilter.java`, `user-service/.../config/SecurityConfig.java`. Endpoints publicos: register, login, validate |

---

## Entidades JPA — Contraste

| Entidad planificada | Servicio | Estado | Justificacion |
|--------------------|----------|--------|---------------|
| Usuario | user-service | Implementada | Campos: id, name, email, passwordHash, bio, createdAt |
| **Rol** | user-service | **Eliminada** | Se decidio no implementar un modelo de roles por simplicidad. Todos los usuarios son regulares |
| Libro | book-service | Implementada | Campos: id, title, author, isbn, genre (String), synopsis, publicationYear |
| **Genero** | book-service | **Eliminada** | El genero se almacena como campo String en `Book`, no como entidad separada. No se necesitaban operaciones CRUD sobre generos |
| Resena | review-service | Implementada | Campos: id, userId, bookId, rating, title, content, createdAt, updatedAt |
| Voto | review-service | Implementada | Campos: id, userId, reviewId, type. Constraint unico en (userId, reviewId) |
| Lista | reading-list-service | Implementada | Campos: id, userId, name, type (enum ListType), isPrivate, createdAt |
| ItemLista | reading-list-service | Implementada | Campos: id, bookId, progress (0-100%), addedAt, finishedAt |
| Seguimiento | social-service | Implementada | Campos: id, followerId, followedId, createdAt |
| **Feed** | social-service | **Eliminada** | El feed se genera al momento via `FeedItemDTO` consultando review-service y reading-list-service. No se persiste para evitar inconsistencias |
| Recomendacion | recommendation-service | Implementada | Campos: id, userId, bookId, score, reason, dismissed, createdAt |
| PreferenciaGenero | recommendation-service | Implementada | Campos: id, userId, genreId (Long), weight |
| Notificacion | notification-service | Implementada | Campos: id, userId, senderId, type, message, isRead, createdAt |
| **ConfiguracionNotif** | notification-service | **Eliminada** | Las preferencias de notificacion se pueden agregar en el futuro. Por ahora todas las notificaciones estan activas por defecto |
| IndiceLibro | search-service | Implementada | Campos: id, bookId, title, author, genre, tags, rating |
| IndiceUsuario | search-service | Implementada | Campos: id, userId, name, bio |
| EstadisticaUsuario | stats-service | Implementada (renombrada) | Ahora se llama `StatSnapshot`. Campos: id, userId, booksRead, booksReading, booksWantToRead, totalReviews, averageRating, favoriteGenre |
| **MetricaGlobal** | stats-service | **Eliminada** | Las metricas globales se calculan on-the-fly via `GlobalStatsDTO` consultando reading-list-service y review-service |
| **RutaConfig** | api-gateway | **Eliminada** | El Gateway funciona con configuracion YAML, no con entidades JPA |
| **LogAcceso** | api-gateway | **Eliminada** | No se implemento logging de accesos en el Gateway por simplicidad |
| AuditEvent | audit-service | **Nueva** (no planificada) | Se agrego un microservicio completo de auditoria con 6 endpoints y 12 tipos de eventos |

---

## Endpoints — Contraste

| Endpoint original | Estado | Observacion |
|-------------------|--------|-------------|
| POST `/api/users/register` | ✅ Implementado | — |
| POST `/api/users/login` | ✅ Implementado | — |
| GET `/api/users/{id}` | ✅ Implementado | — |
| PUT `/api/users/{id}` | ✅ Implementado | Sin campo avatar |
| DELETE `/api/users/{id}` | ✅ Implementado | — |
| GET `/api/books` | ✅ Implementado | Sin paginacion, con filtro por titulo |
| GET `/api/books/{id}` | ✅ Implementado | — |
| POST `/api/books` | ✅ Implementado | Sin control de roles |
| PUT `/api/books/{id}` | ✅ Implementado | — |
| DELETE `/api/books/{id}` | ✅ Implementado | — |
| POST `/api/reviews` | ✅ Implementado | — |
| GET `/api/reviews/book/{bookId}` | ✅ Implementado | — |
| GET `/api/reviews/user/{userId}` | ✅ Implementado | — |
| PUT `/api/reviews/{id}` | ✅ Implementado | — |
| DELETE `/api/reviews/{id}` | ✅ Implementado | — |
| POST `/api/reviews/{id}/vote` | ✅ Implementado | — |
| GET `/api/lists/user/{userId}` | ✅ Implementado | — |
| POST `/api/lists` | ✅ Implementado | — |
| POST `/api/lists/{id}/books` | ✅ Implementado | — |
| PUT `/api/lists/{id}/books/{bookId}` | ✅ Implementado | Ruta: `.../progress?progress=` |
| DELETE `/api/lists/{id}/books/{bookId}` | ✅ Implementado | — |
| POST `/api/social/follow/{userId}` | ⚠️ Cambiado | Ruta: `/api/social/follow?followerId=&followedId=` |
| DELETE `/api/social/follow/{userId}` | ⚠️ Cambiado | Misma ruta con DELETE |
| GET `/api/social/followers/{userId}` | ✅ Implementado | — |
| GET `/api/social/following/{userId}` | ✅ Implementado | — |
| GET `/api/social/feed/{userId}` | ✅ Implementado | — |
| GET `/api/recommendations/{userId}` | ✅ Implementado | — |
| POST `/api/recommendations/refresh/{userId}` | ✅ Implementado | — |
| POST `/api/recommendations/{userId}/dismiss/{bookId}` | ✅ Implementado | — |
| GET `/api/recommendations/trending` | ✅ Implementado | — |
| GET `/api/notifications/{userId}` | ✅ Implementado | — |
| PUT `/api/notifications/{id}/read` | ✅ Implementado | — |
| GET `/api/search?q=` | ✅ Implementado | — |
| GET `/api/search/books` | ✅ Implementado | — |
| GET `/api/search/users?q=` | ✅ Implementado | — |
| POST `/api/search/index/book` | ✅ Implementado | — |
| POST `/api/search/index/user` | ✅ Implementado | — |
| GET `/api/stats/user/{userId}` | ✅ Implementado | — |
| GET `/api/stats/global` | ✅ Implementado | — |
| GET `/api/stats/books/top` | ✅ Implementado | — |
| POST `/api/stats/update/{userId}` | ⚠️ Cambiado | Ruta: `/api/stats/refresh/{userId}` |
| PUT `/api/notifications/read-all/{userId}` | ❌ No implementado | Solo marcacion individual |
| DELETE `/api/notifications/{id}` | ❌ No implementado | — |
| PUT `/api/notifications/config/{userId}` | ❌ No implementado | — |
| GET `/api/stats/user/{userId}/history` | ❌ No implementado | — |
| GET `/api/health` | ⚠️ Parcial | Solo en review, search y recommendation |
| GET `/api/gateway/routes` | ❌ No implementado | — |

---

## Items nuevos no contemplados en el levantamiento original

| Item | Descripcion | Justificacion de inclusion |
|------|-------------|---------------------------|
| **audit-service** | Microservicio completo con 6 endpoints y 12 tipos de eventos de auditoria | Se detecto la necesidad de registrar acciones del sistema para trazabilidad. No estaba en el alcance original pero agrega valor al sistema |
| **eureka-server** | Servidor de descubrimiento Netflix Eureka | El levantamiento original asumia URLs hardcodeadas. Se implemento Eureka para resolver servicios dinamicamente y escalar correctamente |
| **Flyway** | Migraciones SQL versionadas en perfil prod | Se necesito control de esquema en produccion. Hibernate auto-DDL no es seguro en entornos productivos |
| **Gateway filters** | StripPrefix=1 + AddRequestHeader=X-Forwarded-Prefix | Correccion al feedback del profesor: el Gateway solo tenia predicados de ruta, sin filtros configurados |
| **3 profiles** | dev (H2), prod (MySQL), neon (PostgreSQL) | El levantamiento original solo mencionaba MySQL. Se agregaron perfiles para desarrollo local, produccion Docker y despliegue en Render |
| **Docker Compose** | Orquestacion de 22 contenedores | Facilita el levantamiento local completo del sistema |
| **.env.example** | Variables de entorno documentadas | Buena practica de seguridad: evitar credenciales en repositorio |

---

## Estadisticas finales vs planificadas

| Metrica | Planificada (inicio) | Implementada (cierre) | Diferencia |
|---------|---------------------|----------------------|------------|
| Microservicios de negocio | 10 | 11 (+audit) | +1 |
| Infraestructura | 0 | 2 (eureka + gateway) | +2 |
| Endpoints REST | ~50 | 57 | +7 |
| Entidades JPA | ~18 | 15 | -3 |
| Pruebas unitarias | 0 | 132 | +132 |
| Bases de datos | 10 | 10 | 0 |
| Perfiles | 1 (prod) | 3 (dev/prod/neon) | +2 |
