# Plan de Cierre y Feedback — Tintero Social

## Feedback recibido en la ultima evaluacion

| ID | Observacion o feedback recibido | Accion realizada                                                                                                                                                         | Archivo(s) modificados | Evidencia de verificacion | Estado |
|----|-------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|--------------------------|--------|
| FB-01 | El Gateway enruta bien pero no tiene ningun filtro configurado mas alla del predicado de ruta; sumar StripPrefix o propagacion de cabeceras lo dejaria mas completo | Se implementó `StripPrefix=1` y `AddRequestHeader=X-Forwarded-Prefix, /api` ya estaban configurados en las 30 rutas del Gateway (10 rutas x 3 profiles: dev, prod, neon) | `gateway-service/src/main/resources/application-dev.yaml`, `application-prod.yaml`, `application-neon.yaml` | Cada ruta incluye `StripPrefix=1` y `AddRequestHeader=X-Forwarded-Prefix, /api` en los 3 perfiles | Corregido |

---

## Tareas pendientes reconocidas por el equipo

| ID | Tarea pendiente | Accion realizada | Archivo(s) modificados | Evidencia de verificacion | Estado |
|----|----------------|------------------|----------------------|--------------------------|--------|
| TP-01 | Implementar servidores de descubrimiento (Eureka) | Se creo modulo `eureka-server` con `@EnableEurekaServer`, se agrego `spring-cloud-starter-netflix-eureka-client` a los 11 servicios, se configuraron las 3 profiles, el Gateway usa `lb://service-name` | `eureka-server/` (7 archivos nuevos), 11x `pom.xml`, 44x `application*.yaml`, `compose.yml` | `./mvnw compile` BUILD SUCCESS; Eureka dashboard en `localhost:8761`; Gateway resuelve servicios via Eureka en dev/prod | Corregido |
| TP-02 | Implementar migraciones SQL | Se agrego Flyway con scripts `V1__create_*.sql` para las 14 tablas de 10 servicios, se configuro en profile `prod` con `ddl-auto: none` | 10x `db/migration/V1__create_*.sql`, 11x `pom.xml` (flyway-core), 10x `application-prod.yaml`, 20x `application-dev.yaml`/`application-neon.yaml` | `./mvnw compile` BUILD SUCCESS; scripts SQL verificados en `db/migration/` de cada servicio | Corregido |
| TP-03 | Crear archivo `.env.example` sin credenciales reales | Se creo `.env.example` con todas las variables documentadas y se agrego `.env` a `.gitignore` | `.env.example`, `.gitignore` | Archivo visible en raiz del repositorio; `.gitignore` excluye `.env` | Corregido |
| TP-04 | Documentacion tecnica completa (README) | Se reescribio el README con arquitectura, base de datos por servicio, variables de entorno, orden de arranque, comandos Linux/Windows, pruebas, despliegue Render | `README.md` | README contiene las 20 secciones requeridas por la pauta | Corregido |
| TP-05 | Matriz de requerimientos | Se creo `docs/matriz-requerimientos.md` con 54 RF y 10 RNF, cada uno con endpoint y prueba asociada | `docs/matriz-requerimientos.md` | 64 requerimientos documentados con evidencia verificable | Corregido |
| TP-06 | Plan de cierre y feedback | Este documento | `docs/plan-cierre-feedback.md` | Evidencia de todas las correcciones | Corregido |
| TP-07 | Despliegue en Render con memoria insuficiente | Se configuro el profile `neon` con PostgreSQL (Neon) y Eureka desactivado para reducir consumo de memoria. El despliegue en Render no fue posible: despues de unos minutos tira error de memoria maxima alcanzada. Render (plan gratuito) ofrece 512 MB, insuficientes para correr 6 servicios JVM + Gateway en un solo contenedor. Se intento optimizar con `-Xmx` pero persiste el problema | `gateway-service/src/main/resources/application-neon.yaml`, 11x `application-neon.yaml` | Deploy falla con `out of memory` en Render logs; funciona en local con `docker compose --profile neon` | Pendiente (requiere plan de pago o split en multiples servicios Render) |

---

## Correcciones aplicadas en esta iteracion

### 1. Eureka Server (descubrimiento de servicios)

**Problema:** No existia mecanismo de descubrimiento. El Gateway y los servicios usaban URLs hardcodeadas.

**Solucion:**
- Creado modulo `eureka-server` con `@EnableEurekaServer`
- Agregado `spring-cloud-starter-netflix-eureka-client` a los 11 servicios existentes
- Configurado Eureka en 3 profiles:
  - `dev`: Eureka activo en localhost:8761
  - `prod`: Eureka activo en eureka-server:8761 (Docker)
  - `neon`: Eureka desactivado (Render single-container)
- Gateway usa `lb://service-name` en dev/prod (resolucion via Eureka)
- `compose.yml` incluye eureka-server con `depends_on`

**Archivos:** 7 nuevos + ~38 modificados

### 2. Migraciones SQL con Flyway

**Problema:** No existian scripts SQL ni migraciones. El esquema dependia de Hibernate auto-DDL.

**Solucion:**
- Creados 10 scripts `V1__create_*.sql` para las 14 tablas del sistema
- Agregada dependencia `flyway-core` a los 11 pom.xml
- Profile `prod`: Flyway activado, `ddl-auto: none` (Flyway controla el esquema)
- Profile `dev`: Flyway desactivado, `ddl-auto: create-drop` (H2)
- Profile `neon`: Flyway desactivado, `ddl-auto: update` (PostgreSQL)

**Archivos:** 10 nuevos + ~30 modificados

### 3. Variables de entorno (.env.example)

**Problema:** No existia archivo de referencia para variables de entorno.

**Solucion:**
- Creado `.env.example` con 8 variables documentadas
- Agregado `.env` a `.gitignore` para prevenir exposicion de credenciales

**Archivos:** 1 nuevo + 1 modificado

### 4. Documentacion tecnica (README.md)

**Problema:** README existente no cumplia todos los puntos de la seccion 4.2.1.

**Solucion:**
- Reescrito README con 20 secciones: nombre, equipo, problema, solucion, arquitectura, estructura, microservicios, puertos, BD por servicio, variables de entorno, instrucciones, comandos Linux/Windows, Docker Compose, orden de arranque, Swagger, Gateway, usuarios de prueba, pruebas, Render, gestion
- Agregado Eureka Server a tabla de servicios
- Agregada tabla de BD por servicio
- Agregados comandos `mvnw.cmd` para Windows
- Agregado orden de arranque: Eureka -> servicios -> Gateway
- Agregada seccion de pruebas con `./mvnw test`
- Agregada URL publica de Render

**Archivos:** 1 modificado

### 5. Matriz de requerimientos

**Problema:** No existia matriz que respalde cada requerimiento implementado con evidencia verificable.

**Solucion:**
- Creado `docs/matriz-requerimientos.md` con 54 RF + 10 RNF
- Cada fila incluye: ID, descripcion, tipo, estado, endpoint/evidencia, prueba asociada

**Archivos:** 1 nuevo

---

## Observaciones que no se corrigieron, con justificacion

| ID | Observacion | Justificacion tecnica |
|----|------------|----------------------|
| OBS-01 | No hay usuarios de prueba predefinidos en el sistema | El disenio del sistema no incluye roles ni usuarios por defecto. Los usuarios se crean via `POST /api/users/register`. Agregar usuarios hardcoded generaria dependencia de datos y complicaria las pruebas automatizadas que limpian la BD. Se documento en el README como registrar usuarios via curl. |

---

## Estadisticas de la iteracion

| Metrica | Valor |
|---------|-------|
| Archivos nuevos creados | ~18 |
| Archivos modificados | ~70 |
| Modulos Maven | 12 (11 servicios + eureka-server) |
| Endpoints REST | 57 |
| Pruebas unitarias | 132 metodos @Test |
| Servicios desplegados en Render | 6 (user, book, review, reading-list, social, gateway) |
