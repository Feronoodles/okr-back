# OKR Dashboard Backend

Backend Spring Boot para publicar y administrar OKR desde una interfaz web.

## Que problema resuelve

Este proyecto nace de una necesidad muy concreta: un equipo llevaba sus OKR en archivos Excel y queria compartirlos con muchas personas. Pasar el archivo por enlace, pedir que todos lo descargaran y revisarlo fuera del navegador se volvia incomodo y repetitivo.

La solucion fue construir un dashboard web donde los OKR pueden consultarse desde un enlace publico, sin depender de descargar el Excel cada vez. Al mismo tiempo, el backend mantiene autenticacion para administrar la informacion de forma segura.

## Que aporta

- Consulta publica de OKR desde un enlace web.
- Gestion autenticada de key results.
- Sesiones con `accessToken` y `refreshToken`.
- Archivado de key results en lugar de borrado fisico inmediato.
- Despliegue simple con Docker Compose.

## Stack

- Java 17
- Spring Boot 2.7.4
- Spring Security
- Spring Data JPA
- MySQL 8
- Docker Compose
- Flyway disponible para adopcion controlada

## Flujo funcional

- Personas administradoras inician sesion y cargan o administran los key results.
- Personas lectoras pueden abrir el dashboard y revisar los OKR publicados sin autenticarse.
- El endpoint publico principal es `GET /key_result/view_key_results`.

## Autenticacion

La API usa dos tokens:

- `accessToken`: token corto para consumir endpoints protegidos.
- `refreshToken`: token de mayor duracion para renovar sesion.

### Endpoints de autenticacion

- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `POST /auth/logout-all`
- `GET /auth/get_user`

### Nota sobre logout

Cuando se hace logout se revoca el `refreshToken`, pero el `accessToken` emitido sigue funcionando hasta expirar. Esto es normal en un modelo JWT stateless.

## Key Results

### Endpoint publico

- `GET /key_result/view_key_results`

Devuelve solo key results no archivados.

### Administracion autenticada

- `POST /key_result`
- `DELETE /key_result`

El borrado actual no elimina registros fisicamente. En cambio, marca los key results como archivados.

## Variables de entorno

### Base de datos

- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`
- `MYSQL_ROOT_PASSWORD`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### Seguridad

- `API_SECRET`
- `API_SECURITY_ACCESS_TOKEN_EXPIRATION_MINUTES`
- `API_SECURITY_REFRESH_TOKEN_EXPIRATION_DAYS`
- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`

### CORS

- `APP_CORS_ALLOWED_ORIGINS`

Se define como lista separada por comas. Ejemplo:

```env
APP_CORS_ALLOWED_ORIGINS=https://startling-genie-30d1f7.netlify.app,http://localhost:3000
```

### Flyway

- `SPRING_FLYWAY_ENABLED`

Valor por defecto:

```env
SPRING_FLYWAY_ENABLED=false
```

## Docker Compose

El proyecto incluye dos servicios principales en `docker-compose.yml`:

- `mysql`: base de datos MySQL 8
- `app-dos`: backend Spring Boot

### Levantar entorno

```bash
docker compose up -d --build
```

### Ver logs

```bash
docker compose logs -f app-dos
docker compose logs -f mysql
```

### Detener entorno

```bash
docker compose down
```

## Primer arranque con base limpia

Si la base de datos esta vacia y el backend esta configurado con `spring.jpa.hibernate.ddl-auto=validate`, primero debes crear el schema una sola vez con un arranque controlado.

Ejemplo:

```bash
docker compose up -d mysql
docker compose run --rm -e SPRING_JPA_HIBERNATE_DDL_AUTO=update -e SPRING_FLYWAY_ENABLED=false app-dos
docker compose up -d app-dos
```

Despues de eso, el backend vuelve a operar en modo normal con `validate`.

## Flyway

Flyway esta disponible en el proyecto, pero se mantiene desactivado por defecto para no interferir con despliegues ya estabilizados.

Configuracion actual relevante:

- `spring.jpa.hibernate.ddl-auto=validate`
- `spring.flyway.enabled=${SPRING_FLYWAY_ENABLED:false}`
- `spring.flyway.baseline-on-migrate=true`

### Recomendacion actual

- Mantener `SPRING_FLYWAY_ENABLED=false` en operacion normal.
- Activarlo solo en una adopcion controlada o en una fase de migraciones planificada.

### Migraciones

Las migraciones SQL viven en:

```text
src/main/resources/db/migration/
```

## Desarrollo local

Para desarrollo local puedes usar `localhost` en CORS junto con tu frontend local:

```env
APP_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

## Pruebas

El proyecto tiene pruebas de integracion para autenticacion, seguridad y lectura publica de key results.

```bash
mvn clean test
```

## Documentacion adicional

- adopcion de Flyway: `docs/flyway-adoption-plan.md`
- checklist SQL de preflight: `scripts/sql/flyway_preflight_mysql8.sql`
