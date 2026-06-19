# Flyway Adoption Plan

Este proyecto tiene Flyway disponible, pero desactivado por defecto en runtime.

Estado actual:

- `spring.jpa.hibernate.ddl-auto=validate`
- `spring.flyway.enabled=${SPRING_FLYWAY_ENABLED:false}`
- `spring.flyway.baseline-on-migrate=true`
- migracion base: `src/main/resources/db/migration/V1__initial_schema.sql`

## Objetivo

Adoptar Flyway en una base MySQL 8 ya existente sin romper produccion.

## Regla principal

No activar Flyway primero en produccion. Primero se prueba sobre una copia de produccion en staging.

## Preflight

Antes de activar Flyway:

1. Sacar backup de la base.
2. Ejecutar `scripts/sql/flyway_preflight_mysql8.sql` contra la base real.
3. Comparar el resultado con el schema esperado por:
   - `src/main/resources/db/migration/V1__initial_schema.sql`
   - `src/main/java/com/example/okr/entities/User.java`
   - `src/main/java/com/example/okr/entities/KeyResult.java`
   - `src/main/java/com/example/okr/entities/RefreshToken.java`
4. Confirmar que no exista `flyway_schema_history` previa o, si existe, entender su estado.
5. Confirmar que el usuario de BD tenga permisos para crear y leer `flyway_schema_history`.

## Staging

1. Clonar produccion a una BD staging MySQL 8.
2. Desplegar el mismo artefacto del backend.
3. Activar Flyway solo en staging con variable de entorno:

```bash
SPRING_FLYWAY_ENABLED=true
```

4. Levantar la app y revisar logs.
5. Confirmar que:
   - Flyway crea `flyway_schema_history`
   - registra baseline en version `1`
   - Hibernate no falla con `validate`
6. Probar flujo minimo:
   - login
   - refresh
   - `GET /key_result/view_key_results`
   - creacion de KR autenticada

## Produccion

1. Tomar backup consistente antes del deploy.
2. Repetir exactamente la estrategia validada en staging.
3. Activar temporalmente:

```bash
SPRING_FLYWAY_ENABLED=true
```

4. Vigilar logs del arranque.
5. Ejecutar smoke tests apenas arranque.

## Despues del baseline exitoso

Una vez que staging y produccion ya tengan `flyway_schema_history`, el siguiente trabajo es:

1. crear nuevas migraciones `V2`, `V3`, etc.
2. evitar cambios manuales de schema fuera de Flyway
3. evaluar quitar `baseline-on-migrate=true` para uso normal

## No hacer

- No activar Flyway por primera vez sobre una BD incorrecta.
- No asumir que `V1` representa exactamente el schema real sin compararlo.
- No desplegar directo a produccion sin probar baseline en staging.
