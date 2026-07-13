# Matriz de Requerimientos — Tintero Social

## Requerimientos Funcionales

| ID | Requerimiento | Tipo | Estado | Endpoint / Evidencia | Prueba Asociada |
|----|--------------|------|--------|---------------------|-----------------|
| | **User Service** | | | | |
| RF-01 | Registrar usuario con datos obligatorios (nombre, email, password) | Funcional | Implementado | `POST /api/users/register` | `UserServiceTest.register_shouldSaveUser_whenEmailIsNotTaken` |
| RF-02 | Autenticar usuario y retornar token JWT | Funcional | Implementado | `POST /api/users/login` | `UserServiceTest.login_shouldReturnToken_whenCredentialsAreValid` |
| RF-03 | Validar token JWT | Funcional | Implementado | `GET /api/users/validate?token=` | `JwtUtilTest.generateAndValidateToken_shouldReturnTrue_whenTokenIsValid` |
| RF-04 | Listar todos los usuarios registrados | Funcional | Implementado | `GET /api/users` | `UserServiceTest.findAll_shouldReturnList_whenUsersExist` |
| RF-05 | Obtener usuario por ID | Funcional | Implementado | `GET /api/users/{id}` | `UserServiceTest.findById_shouldReturnUser_whenExists` |
| RF-06 | Actualizar datos de usuario (nombre, bio) | Funcional | Implementado | `PUT /api/users/{id}` | `UserServiceTest.update_shouldUpdateNameAndBio_whenUserExists` |
| RF-07 | Eliminar usuario | Funcional | Implementado | `DELETE /api/users/{id}` | `UserServiceTest.delete_shouldRemoveUser_whenExists` |
| | **Book Service** | | | | |
| RF-08 | Listar libros con filtro por titulo | Funcional | Implementado | `GET /api/books?title=` | `BookServiceTest.listAll_shouldReturnAllBooks_whenBooksExist` |
| RF-09 | Obtener libro por ID | Funcional | Implementado | `GET /api/books/{id}` | `BookServiceTest.findById_shouldReturnBook_whenExists` |
| RF-10 | Crear libro en el catalogo | Funcional | Implementado | `POST /api/books` | `BookServiceTest.save_shouldCreateBook_whenIsbnIsNotTaken` |
| RF-11 | Actualizar libro existente | Funcional | Implementado | `PUT /api/books/{id}` | `BookServiceTest.update_shouldModifyAllFields_whenBookExists` |
| RF-12 | Eliminar libro del catalogo | Funcional | Implementado | `DELETE /api/books/{id}` | `BookServiceTest.delete_shouldRemoveBook_whenExists` |
| | **Review Service** | | | | |
| RF-13 | Crear resena para un libro | Funcional | Implementado | `POST /api/reviews` | `ReviewServiceTest.createReview_shouldSave_whenDataIsValid` |
| RF-14 | Listar resenas de un libro | Funcional | Implementado | `GET /api/reviews/book/{bookId}` | `ReviewServiceTest.getReviewsByBook_shouldReturnList` |
| RF-15 | Listar resenas de un usuario | Funcional | Implementado | `GET /api/reviews/user/{userId}` | `ReviewServiceTest.getReviewsByUser_shouldReturnList` |
| RF-16 | Editar resena (solo el autor) | Funcional | Implementado | `PUT /api/reviews/{id}?userId=` | `ReviewServiceTest.updateReview_shouldModifyFields_whenOwner` |
| RF-17 | Eliminar resena (solo el autor) | Funcional | Implementado | `DELETE /api/reviews/{id}?userId=` | `ReviewServiceTest.deleteReview_shouldRemove_whenOwner` |
| RF-18 | Votar resena como util | Funcional | Implementado | `POST /api/reviews/{id}/vote` | `ReviewServiceTest.voteReview_shouldSaveVote_whenValid` |
| | **Reading List Service** | | | | |
| RF-19 | Crear lista de lectura personalizada | Funcional | Implementado | `POST /api/lists` | `ReadingListServiceTest.createList_shouldSave_whenUserExists` |
| RF-20 | Listar listas de un usuario | Funcional | Implementado | `GET /api/lists/user/{userId}` | `ReadingListServiceTest.getListsByUser_shouldReturnLists` |
| RF-21 | Obtener lista por ID | Funcional | Implementado | `GET /api/lists/{id}` | `ReadingListServiceTest.getListById_shouldReturnList_whenExists` |
| RF-22 | Eliminar lista de lectura | Funcional | Implementado | `DELETE /api/lists/{id}` | `ReadingListServiceTest.deleteList_shouldRemove_whenExists` |
| RF-23 | Agregar libro a una lista | Funcional | Implementado | `POST /api/lists/{id}/books` | `ReadingListServiceTest.addBook_shouldAdd_whenBookNotInList` |
| RF-24 | Actualizar progreso de lectura (0-100%) | Funcional | Implementado | `PUT /api/lists/{listId}/books/{bookId}/progress?progress=` | `ReadingListServiceTest.updateProgress_shouldSetProgress` |
| RF-25 | Remover libro de una lista | Funcional | Implementado | `DELETE /api/lists/{listId}/books/{bookId}` | `ReadingListServiceTest.removeBook_shouldDelete_whenItemExists` |
| | **Social Service** | | | | |
| RF-26 | Seguir a otro usuario | Funcional | Implementado | `POST /api/social/follow?followerId=&followedId=` | `SocialServiceTest.follow_shouldSave_whenAllChecksPass` |
| RF-27 | Dejar de seguir a un usuario | Funcional | Implementado | `DELETE /api/social/follow?followerId=&followedId=` | `SocialServiceTest.unfollow_shouldRemove_whenFollowExists` |
| RF-28 | Obtener seguidores de un usuario | Funcional | Implementado | `GET /api/social/followers/{userId}` | `SocialServiceTest.getFollowers_shouldReturnList` |
| RF-29 | Obtener usuarios que sigue un usuario | Funcional | Implementado | `GET /api/social/following/{userId}` | `SocialServiceTest.getFollowing_shouldReturnList` |
| RF-30 | Obtener feed de actividad (resenas y listas de seguidos) | Funcional | Implementado | `GET /api/social/feed/{userId}` | `SocialServiceTest.getFeed_shouldAggregateReviewsAndLists_whenFollowingUsers` |
| RF-31 | Obtener estadisticas de seguimiento | Funcional | Implementado | `GET /api/social/stats/{userId}` | `SocialServiceTest.getStats_shouldReturnCounts` |
| | **Recommendation Service** | | | | |
| RF-32 | Obtener recomendaciones activas de un usuario | Funcional | Implementado | `GET /api/recommendations/{userId}` | `RecommendationServiceTest.getRecommendations_shouldReturnList_whenExists` |
| RF-33 | Refrescar recomendaciones segun preferencias | Funcional | Implementado | `POST /api/recommendations/refresh/{userId}` | `RecommendationServiceTest.refreshRecommendations_shouldNotThrow_whenPreferencesExist` |
| RF-34 | Descartar una recomendacion | Funcional | Implementado | `POST /api/recommendations/{userId}/dismiss/{bookId}` | `RecommendationServiceTest.dismissRecommendation_shouldSetDismissed_whenFound` |
| RF-35 | Obtener libros trending (mejor valorados) | Funcional | Implementado | `GET /api/recommendations/trending` | `RecommendationServiceTest.getTrending_shouldReturnTop10SortedByScore` |
| RF-36 | Agregar preferencia de genero | Funcional | Implementado | `POST /api/recommendations/preferences` | `RecommendationServiceTest.addGenrePreference_shouldSave_whenNotDuplicate` |
| | **Notification Service** | | | | |
| RF-37 | Crear notificacion interna | Funcional | Implementado | `POST /api/notifications` | `NotificationServiceTest.create_shouldSaveAndReturnDTO_whenSenderExists` |
| RF-38 | Listar notificaciones de un usuario | Funcional | Implementado | `GET /api/notifications/{userId}` | `NotificationServiceTest.getByUserId_shouldReturnListWithSenderNames` |
| RF-39 | Marcar notificacion como leida | Funcional | Implementado | `PUT /api/notifications/{id}/read` | `NotificationServiceTest.markAsRead_shouldSetRead_whenExists` |
| | **Search Service** | | | | |
| RF-40 | Busqueda global de libros y usuarios | Funcional | Implementado | `GET /api/search?q=` | `SearchServiceTest.globalSearch_shouldCombineBooksAndUsers` |
| RF-41 | Buscar libros con filtros (genero, rating) | Funcional | Implementado | `GET /api/search/books?q=&genre=&rating=` | `SearchServiceTest.searchBooksFiltered_shouldFilterByGenreAndRating` |
| RF-42 | Buscar usuarios por nombre | Funcional | Implementado | `GET /api/search/users?q=` | `SearchServiceTest.searchUsers_shouldReturnMatchingUsers` |
| RF-43 | Indexar libro en el indice de busqueda | Funcional | Implementado | `POST /api/search/index/book` | `SearchServiceTest.indexBook_shouldCreateNew_whenNotExists` |
| RF-44 | Indexar usuario en el indice de busqueda | Funcional | Implementado | `POST /api/search/index/user` | `SearchServiceTest.indexUser_shouldCreateNew_whenNotExists` |
| | **Stats Service** | | | | |
| RF-45 | Obtener estadisticas de lectura de un usuario | Funcional | Implementado | `GET /api/stats/user/{userId}` | `StatsServiceTest.getUserStats_shouldCreateSnapshotWithComputedStats` |
| RF-46 | Obtener estadisticas globales de la plataforma | Funcional | Implementado | `GET /api/stats/global` | `StatsServiceTest.getGlobalStats_shouldReturnStats` |
| RF-47 | Obtener ranking de libros mas leidos | Funcional | Implementado | `GET /api/stats/books/top` | `StatsServiceTest.getTopBooks_shouldReturnTopBooks` |
| RF-48 | Recalcular estadisticas de un usuario | Funcional | Implementado | `POST /api/stats/refresh/{userId}` | `StatsServiceTest.getUserStats_shouldUpdateExistingSnapshot` |
| | **Audit Service** | | | | |
| RF-49 | Registrar evento de auditoria | Funcional | Implementado | `POST /api/audit/event` | `AuditServiceTest.registerEvent_shouldSaveAndReturnDTO` |
| RF-50 | Listar todos los eventos de auditoria | Funcional | Implementado | `GET /api/audit/events` | `AuditServiceTest.getAllEvents_shouldReturnList` |
| RF-51 | Filtrar eventos por usuario | Funcional | Implementado | `GET /api/audit/events/user/{userId}` | `AuditServiceTest.getEventsByUser_shouldReturnFilteredList` |
| RF-52 | Filtrar eventos por tipo | Funcional | Implementado | `GET /api/audit/events/type/{eventType}` | `AuditServiceTest.getEventsByType_shouldReturnFilteredList` |
| RF-53 | Filtrar eventos por recurso | Funcional | Implementado | `GET /api/audit/events/resource/{type}/{id}` | `AuditServiceTest.getEventsByResource_shouldReturnFilteredList` |
| RF-54 | Eliminar evento de auditoria | Funcional | Implementado | `DELETE /api/audit/events/{id}` | `AuditServiceTest.deleteEvent_shouldRemove_whenExists` |

---

## Requerimientos No Funcionales

| ID | Requerimiento | Tipo | Estado | Endpoint / Evidencia | Prueba Asociada |
|----|--------------|------|--------|---------------------|-----------------|
| RNF-01 | No exponer credenciales en GitHub | No funcional | Implementado | `.env.example` en raiz + `.gitignore` excluye `.env` | Revision de repositorio |
| RNF-02 | API Gateway centraliza todas las peticiones HTTP | No funcional | Implementado | `gateway-service` (puerto 8080) con 10 rutas `lb://` | `gateway-service/src/main/resources/application-dev.yaml` |
| RNF-03 | Descubrimiento automatico de servicios | No funcional | Implementado | `eureka-server` (puerto 8761) + `spring-cloud-starter-netflix-eureka-client` en 11 servicios | `pom.xml` raiz + `eureka-server/` |
| RNF-04 | Documentacion Swagger/OpenAPI en cada servicio | No funcional | Implementado | `springdoc-openapi-starter-webmvc-ui` + `OpenApiConfig.java` en 10 servicios | `localhost:{port}/swagger-ui/index.html` |
| RNF-05 | Migraciones SQL versionadas en produccion | No funcional | Implementado | Flyway con `V1__create_*.sql` en 10 servicios | `{service}/src/main/resources/db/migration/` |
| RNF-06 | Perfiles de configuracion por entorno | No funcional | Implementado | Profile `dev` (H2), `prod` (MySQL), `neon` (PostgreSQL) | `application-dev.yaml`, `application-prod.yaml`, `application-neon.yaml` |
| RNF-07 | Despliegue remoto en Render | No funcional | Implementado | Dockerfile multi-stage + `docker-entrypoint.sh` | `https://tintero-social.onrender.com` |
| RNF-08 | Pruebas unitarias por servicio | No funcional | Implementado | 26 archivos de test, 132 metodos `@Test` en 10 servicios | `./mvnw test` |
| RNF-09 | Autenticacion basada en JWT | No funcional | Implementado | `JwtUtil`, `JwtAuthFilter`, `SecurityConfig` en user-service | `UserServiceTest.login_shouldReturnToken_whenCredentialsAreValid` |
| RNF-10 | Contrasenas hasheadas con algoritmo seguro | No funcional | Implementado | `BCryptPasswordEncoder` en registro y login | `UserServiceTest.register_shouldSaveUser_whenEmailIsNotTaken` |
