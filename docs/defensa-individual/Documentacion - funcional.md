# Documentación Funcional — Tintero Social

> Plataforma Social de Lectura  
> DSY1103 Desarrollo FullStack 1 · DuocUC · 2025

---

## 1. Problema que resuelve

Los lectores no cuentan con una plataforma centralizada que les permita registrar su historial de lecturas, escribir reseñas y recibir recomendaciones personalizadas basadas en su actividad y la de su red social. Esto genera desorganización, lecturas duplicadas y descubrimiento de libros limitado a canales comerciales.

Las plataformas existentes como Goodreads tienen interfaces desactualizadas, están en inglés y no consideran el contexto de la comunidad latinoamericana.

---

## 2. Actores o perfiles

| Actor | Descripción |
|---|---|
| **Lector visitante** | Usuario no registrado. Puede explorar el catálogo y leer reseñas públicas, pero no puede interactuar. |
| **Lector registrado** | Usuario principal. Registra libros, escribe reseñas, sigue a otros lectores y recibe recomendaciones. |
| **Administrador** | Gestiona el catálogo de libros, modera contenido y accede a métricas globales de la plataforma. |

---

## 3. Requerimientos funcionales

| ID | Descripción | Actor |
|---|---|---|
| RF-01 | Registrar un nuevo usuario con nombre, correo electrónico y contraseña encriptada. | Visitante |
| RF-02 | Autenticar a un usuario existente y retornar un token JWT válido. | Lector registrado |
| RF-03 | Actualizar los datos del perfil propio (nombre, biografía, avatar). | Lector registrado |
| RF-04 | Eliminar la cuenta propia y todos los datos asociados. | Lector registrado |
| RF-05 | Buscar libros por título, autor, género o ISBN con paginación. | Todos |
| RF-06 | Agregar un libro al catálogo con título, autor, ISBN, género y sinopsis. | Administrador |
| RF-07 | Agregar un libro a una lista personal (por leer, leyendo, leído). | Lector registrado |
| RF-08 | Actualizar el progreso de lectura de un libro en una lista. | Lector registrado |
| RF-09 | Eliminar un libro de una lista de lectura personal. | Lector registrado |
| RF-10 | Crear una reseña con calificación (1–5 estrellas) y texto sobre un libro leído. | Lector registrado |
| RF-11 | Editar o eliminar una reseña propia. | Lector registrado |
| RF-12 | Votar una reseña de otro usuario como "útil". | Lector registrado |
| RF-13 | Seguir a otro lector para ver su actividad en el feed personal. | Lector registrado |
| RF-14 | Dejar de seguir a un lector previamente seguido. | Lector registrado |
| RF-15 | Visualizar el feed de actividad con las acciones recientes de usuarios seguidos. | Lector registrado |
| RF-16 | Recibir recomendaciones de libros basadas en géneros favoritos e historial. | Lector registrado |
| RF-17 | Descartar una recomendación para que no vuelva a aparecer. | Lector registrado |
| RF-18 | Visualizar estadísticas personales (libros leídos, géneros favoritos, racha de lectura). | Lector registrado |
| RF-19 | Recibir notificaciones internas al obtener un nuevo seguidor o votos en reseñas. | Lector registrado |
| RF-20 | Marcar notificaciones como leídas de forma individual o masiva. | Lector registrado |

---

## 4. Flujos principales

### 4.1 Registro e inicio de sesión

1. El visitante accede a la plataforma y completa el formulario de registro (nombre, correo, contraseña).
2. El sistema valida los datos y crea la cuenta (HTTP 201).
3. El usuario inicia sesión con sus credenciales.
4. El sistema retorna un token JWT que debe incluirse en todas las peticiones posteriores.

### 4.2 Agregar un libro a una lista de lectura

1. El lector busca un libro por título, autor o género.
2. Selecciona un libro del catálogo.
3. Lo agrega a una de sus listas: "Por leer", "Leyendo" o "Leído".
4. El sistema registra la fecha de incorporación.
5. Si el libro ya está en alguna lista, el sistema retorna HTTP 409.

### 4.3 Escribir y compartir una reseña

1. El lector selecciona un libro de su lista "Leídos".
2. Escribe una reseña con título, contenido y calificación (1 a 5 estrellas).
3. El sistema publica la reseña (HTTP 201) y queda visible públicamente de inmediato.
4. Otros usuarios pueden votar la reseña como "útil".
5. Al recibir votos, el autor recibe una notificación interna.

### 4.4 Seguir a otro lector

1. El lector busca a otro usuario por nombre.
2. Hace clic en "Seguir".
3. El sistema registra la relación (HTTP 201).
4. El usuario seguido recibe una notificación interna sobre el nuevo seguidor.
5. Las acciones del usuario seguido comienzan a aparecer en el feed del lector.

### 4.5 Recibir y gestionar recomendaciones

1. El sistema genera recomendaciones basadas en el historial de lectura y géneros favoritos.
2. Si el usuario tiene menos de 3 libros en su historial, el sistema muestra libros en tendencia global.
3. El usuario puede descartar una recomendación para que no vuelva a aparecer.
4. El usuario puede regenerar sus recomendaciones manualmente.

---

## 5. Reglas de negocio

- Un usuario solo puede tener **una reseña por libro**. Intentar crear una segunda retorna HTTP 409.
- El **rating de una reseña** debe estar entre 1 y 5. Un valor fuera de este rango retorna HTTP 400.
- Solo el **autor de una reseña** puede editarla o eliminarla. Otro usuario retorna HTTP 403.
- Un usuario **no puede seguirse a sí mismo**. El sistema retorna HTTP 400.
- Un usuario **no puede votar dos veces** la misma reseña. El sistema retorna HTTP 409.
- Las **contraseñas nunca se almacenan en texto plano**; siempre se encriptan con BCrypt.
- Un libro **no puede aparecer dos veces** en las listas de un mismo usuario. El sistema retorna HTTP 409.
- Las recomendaciones descartadas **no vuelven a aparecer** en la lista del usuario.
- Los libros en tendencia se muestran cuando el usuario **no tiene suficiente historial** (menos de 3 libros).

---

## 6. Estados relevantes

### Estado de un libro en las listas de lectura

```
WANT_TO_READ → READING → READ
```

| Estado | Descripción |
|---|---|
| `WANT_TO_READ` | Por leer: el usuario planea leer este libro. |
| `READING` | Leyendo: el usuario está leyendo el libro actualmente. |
| `READ` | Leído: el usuario ha terminado de leer el libro. |

### Estado de una notificación

| Estado | Descripción |
|---|---|
| `leida = false` | La notificación aún no ha sido vista por el usuario. |
| `leida = true` | La notificación fue marcada como leída. |

### Estado de una recomendación

| Estado | Descripción |
|---|---|
| `dismissed = false` | La recomendación está activa y visible para el usuario. |
| `dismissed = true` | El usuario descartó la recomendación; ya no aparece en su lista. |

---

## 7. Restricciones del dominio

- El **ISBN de un libro debe ser único** en el catálogo. Intentar agregar un libro con ISBN duplicado retorna HTTP 409.
- El **correo electrónico de un usuario debe ser único**. Un correo ya registrado retorna HTTP 409.
- Toda petición a endpoints protegidos debe incluir un **token JWT válido** en la cabecera `Authorization`. Sin token retorna HTTP 401.
- La comunicación entre microservicios se realiza **únicamente a través de APIs REST**; ningún servicio accede directamente a la base de datos de otro.
- Cada microservicio opera con su **propia base de datos independiente**.

---

## 8. Ejemplos de uso

### Registrar un usuario

```http
POST /api/users/register
Content-Type: application/json

{
  "nombre": "María González",
  "email": "maria@example.com",
  "password": "miPassword123"
}
```

Respuesta esperada: HTTP 201 con el ID del usuario creado.

### Crear una reseña

```http
POST /api/reviews
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "bookId": 10,
  "rating": 4,
  "title": "Muy buena lectura",
  "content": "Me encantó el desarrollo de personajes."
}
```

Respuesta esperada: HTTP 201 con la reseña creada.

### Agregar un libro a "Por leer"

```http
POST /api/lists/1/books
Authorization: Bearer <token>
Content-Type: application/json

{
  "bookId": 10,
  "type": "WANT_TO_READ"
}
```

Respuesta esperada: HTTP 201 con la lista actualizada.

### Buscar libros

```http
GET /api/search/books?q=harry&genre=Fantasía&rating=4
```

Respuesta esperada: HTTP 200 con lista de libros que coinciden.

---

## 9. Datos de prueba sugeridos

### Usuarios

| Nombre | Email | Contraseña |
|---|---|---|
| María González | maria@test.com | Test1234! |
| Juan Pérez | juan@test.com | Test1234! |
| Admin Tintero | admin@tintero.cl | Admin1234! |

### Libros

| Título | Autor | ISBN | Género |
|---|---|---|---|
| El Principito | Antoine de Saint-Exupéry | 978-0156012195 | Ficción |
| Cien años de soledad | Gabriel García Márquez | 978-0307474728 | Realismo mágico |
| El nombre del viento | Patrick Rothfuss | 978-0756404741 | Fantasía |
| 1984 | George Orwell | 978-0451524935 | Distopía |
| Sapiens | Yuval Noah Harari | 978-0062316110 | No ficción |

### Flujo de prueba completo

1. Registrar usuario `maria@test.com`.
2. Iniciar sesión y guardar el token JWT.
3. Agregar el libro con ID 1 a la lista "Por leer".
4. Cambiar el estado del libro a "Leyendo".
5. Cambiar el estado a "Leído".
6. Escribir una reseña con rating 5.
7. Registrar usuario `juan@test.com` e iniciar sesión.
8. Seguir a María.
9. Votar la reseña de María como "útil".
10. Verificar que María recibió notificaciones.