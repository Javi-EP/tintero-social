[README.md](https://github.com/user-attachments/files/27982812/README.md)
# 📚 Tintero Social

> Plataforma social de lectura — DSY1103 · DuocUC

Tintero Social es una aplicación backend basada en **microservicios** que permite a los usuarios gestionar sus lecturas, descubrir libros, escribir reseñas y conectar con otros lectores. Construida con Java 21 y Spring Boot 4.

---

## 🏗️ Arquitectura

El proyecto está organizado como un **monorepo Maven multi-módulo**, donde cada módulo representa un microservicio independiente con su propia responsabilidad:

| Servicio | Descripción |
|---|---|
| `user-service` | Gestión de usuarios, autenticación y perfiles |
| `book-service` | Catálogo de libros e información bibliográfica |
| `review-service` | Reseñas y valoraciones de libros |
| `reading-list-service` | Listas de lectura personales (leídos, leyendo, pendientes) |
| `social-service` | Seguimiento entre usuarios, actividad social |
| `recommendation-service` | Motor de recomendaciones personalizadas |
| `notification-service` | Notificaciones y alertas a usuarios |
| `search-service` | Búsqueda de libros y usuarios |
| `stats-service` | Estadísticas de lectura y actividad |
| `audit-service` | Registro de auditoría de acciones del sistema |

---

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 4**
- **Maven** (estructura multi-módulo)

---

## 🚀 Inicio rápido

### Requisitos previos

- Java 21+
- Maven 3.9+ (o usar el wrapper incluido `./mvnw`)

### Clonar el repositorio

```bash
git clone https://github.com/Javi-EP/tintero-social.git
cd tintero-social
```

### Compilar todos los módulos

```bash
./mvnw clean install
```

### Ejecutar un servicio específico

```bash
cd user-service
../mvnw spring-boot:run
```

---

## 📁 Estructura del proyecto

```
tintero-social/
├── audit-service/
├── book-service/
├── notification-service/
├── reading-list-service/
├── recommendation-service/
├── review-service/
├── search-service/
├── social-service/
├── stats-service/
├── user-service/
├── pom.xml          # POM raíz (multi-módulo)
├── mvnw             # Maven Wrapper (Linux/macOS)
└── mvnw.cmd         # Maven Wrapper (Windows)
```

---

## 👥 Equipo

Proyecto académico desarrollado para el curso **DSY1103** de [DuocUC](https://www.duoc.cl).

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos.
