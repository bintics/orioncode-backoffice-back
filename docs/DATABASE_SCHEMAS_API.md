# API de Schemas de Base de Datos

Este documento describe los endpoints disponibles para gestionar schemas de base de datos en el IDP.

## Endpoints Disponibles

### 1. Buscar Schemas (con paginación y filtros)

**Endpoint:** `GET /database-schemas`

**Descripción:** Busca schemas de base de datos con soporte para filtros dinámicos y paginación.

**Parámetros de Query:**

| Parámetro | Tipo | Requerido | Default | Descripción |
|-----------|------|-----------|---------|-------------|
| `filter` | String | No | - | Campo sobre el que buscar: `name`, `serverId`, `description`, `owner` |
| `search` | String | No | - | Valor a buscar (búsqueda parcial, case-insensitive) |
| `page` | Integer | No | 1 | Número de página (inicia en 1) |
| `size` | Integer | No | 20 | Tamaño de página |
| `sortBy` | String | No | name | Campo para ordenar |
| `sortDir` | String | No | asc | Dirección de ordenamiento (`asc`/`desc`) |

**Ejemplo de Solicitud:**

```bash
# Búsqueda simple (busca en todos los campos)
GET /database-schemas?search=banking&page=1&size=20

# Búsqueda con filtro específico (por servidor)
GET /database-schemas?filter=serverId&search=db-srv-001&page=1&size=20

# Búsqueda con filtro (por owner)
GET /database-schemas?filter=owner&search=pg_admin&page=1&size=20

# Búsqueda con ordenamiento
GET /database-schemas?filter=name&search=core&sortBy=sizeInMB&sortDir=desc
```

**Respuesta Exitosa (200 OK):**

```json
{
  "data": [
    {
      "id": "schema-001",
      "name": "banking_core",
      "serverId": "db-srv-004",
      "description": "Schema principal del core bancario con datos transaccionales",
      "owner": "oracle_admin",
      "tablesCount": 350,
      "viewsCount": 125,
      "proceduresCount": 220,
      "functionsCount": 180,
      "lastModified": "2024-12-16T19:30:00",
      "sizeInMB": 45600.50,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-12-16T19:30:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 1,
    "totalPages": 1
  },
  "metadata": {
    "filters": [
      "name",
      "serverId",
      "description",
      "owner"
    ]
  }
}
```

---

### 2. Obtener Schema por ID

**Endpoint:** `GET /database-schemas/{id}`

**Descripción:** Retorna un schema de base de datos específico por su ID.

**Parámetros de Path:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `id` | String | Sí | ID del schema |

**Ejemplo de Solicitud:**

```bash
GET /database-schemas/schema-001
```

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "schema-001",
  "name": "banking_core",
  "serverId": "db-srv-004",
  "description": "Schema principal del core bancario con datos transaccionales",
  "owner": "oracle_admin",
  "tablesCount": 350,
  "viewsCount": 125,
  "proceduresCount": 220,
  "functionsCount": 180,
  "lastModified": "2024-12-16T19:30:00",
  "sizeInMB": 45600.50,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-12-16T19:30:00"
}
```

**Respuesta de Error (404 Not Found):**

```json
{
  "message": "Schema de base de datos no encontrado con ID: schema-001"
}
```

---

### 3. Crear Schema

**Endpoint:** `POST /database-schemas`

**Descripción:** Crea un nuevo schema de base de datos.

**Body (JSON):**

```json
{
  "id": "schema-021",
  "name": "customer_portal",
  "serverId": "db-srv-001",
  "description": "Schema para portal de clientes",
  "owner": "pg_admin",
  "tablesCount": 45,
  "viewsCount": 18,
  "proceduresCount": 25,
  "functionsCount": 20,
  "lastModified": "2024-12-16T19:30:00",
  "sizeInMB": 3200.75
}
```

**Campos del Body:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `id` | String | Sí | ID único del schema (proporcionado por el cliente) |
| `name` | String | Sí | Nombre del schema |
| `serverId` | String | Sí | ID del servidor donde reside el schema |
| `description` | String | No | Descripción del schema |
| `owner` | String | Sí | Usuario/rol propietario del schema |
| `tablesCount` | Integer | No | Número de tablas (≥ 0) |
| `viewsCount` | Integer | No | Número de vistas (≥ 0) |
| `proceduresCount` | Integer | No | Número de procedimientos almacenados (≥ 0) |
| `functionsCount` | Integer | No | Número de funciones (≥ 0) |
| `lastModified` | DateTime | No | Fecha de última modificación |
| `sizeInMB` | Double | No | Tamaño en MB (≥ 0) |

**Respuesta Exitosa (201 Created):**

```json
{
  "id": "schema-021",
  "name": "customer_portal",
  "serverId": "db-srv-001",
  "description": "Schema para portal de clientes",
  "owner": "pg_admin",
  "tablesCount": 45,
  "viewsCount": 18,
  "proceduresCount": 25,
  "functionsCount": 20,
  "lastModified": "2024-12-16T19:30:00",
  "sizeInMB": 3200.75,
  "createdAt": "2024-12-16T19:35:00",
  "updatedAt": "2024-12-16T19:35:00"
}
```

---

### 4. Actualizar Schema

**Endpoint:** `PUT /database-schemas/{id}`

**Descripción:** Actualiza un schema de base de datos existente.

**Parámetros de Path:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `id` | String | Sí | ID del schema |

**Body (JSON):**

```json
{
  "name": "customer_portal_v2",
  "serverId": "db-srv-001",
  "description": "Schema actualizado para portal de clientes versión 2",
  "owner": "pg_admin",
  "tablesCount": 52,
  "viewsCount": 22,
  "proceduresCount": 30,
  "functionsCount": 25,
  "lastModified": "2024-12-16T20:00:00",
  "sizeInMB": 3850.90
}
```

**Campos del Body:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `name` | String | Sí | Nombre del schema (2-200 caracteres) |
| `serverId` | String | Sí | ID del servidor (máx. 100 caracteres) |
| `description` | String | No | Descripción (máx. 1000 caracteres) |
| `owner` | String | Sí | Propietario (máx. 100 caracteres) |
| `tablesCount` | Integer | No | Número de tablas (≥ 0) |
| `viewsCount` | Integer | No | Número de vistas (≥ 0) |
| `proceduresCount` | Integer | No | Número de procedimientos (≥ 0) |
| `functionsCount` | Integer | No | Número de funciones (≥ 0) |
| `lastModified` | DateTime | No | Fecha de última modificación |
| `sizeInMB` | Double | No | Tamaño en MB (≥ 0) |

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "schema-021",
  "name": "customer_portal_v2",
  "serverId": "db-srv-001",
  "description": "Schema actualizado para portal de clientes versión 2",
  "owner": "pg_admin",
  "tablesCount": 52,
  "viewsCount": 22,
  "proceduresCount": 30,
  "functionsCount": 25,
  "lastModified": "2024-12-16T20:00:00",
  "sizeInMB": 3850.90,
  "createdAt": "2024-12-16T19:35:00",
  "updatedAt": "2024-12-16T20:05:00"
}
```

---

### 5. Eliminar Schema

**Endpoint:** `DELETE /database-schemas/{id}`

**Descripción:** Elimina un schema de base de datos por su ID.

**Parámetros de Path:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `id` | String | Sí | ID del schema |

**Ejemplo de Solicitud:**

```bash
DELETE /database-schemas/schema-021
```

**Respuesta Exitosa (204 No Content):**

Sin contenido en el body.

**Respuesta de Error (404 Not Found):**

```json
{
  "message": "Schema de base de datos no encontrado con ID: schema-021"
}
```

---

## Casos de Uso

### Listar schemas de un servidor específico

```bash
GET /database-schemas?filter=serverId&search=db-srv-001&page=1&size=50
```

### Buscar schemas por propietario

```bash
GET /database-schemas?filter=owner&search=pg_admin
```

### Buscar schemas grandes (ordenados por tamaño)

```bash
GET /database-schemas?sortBy=sizeInMB&sortDir=desc&size=10
```

### Buscar schemas con muchas tablas

```bash
GET /database-schemas?sortBy=tablesCount&sortDir=desc&size=20
```

---

## Ejemplos de Uso con cURL

### Crear un schema

```bash
curl -X POST http://localhost:8080/database-schemas \
  -H "Content-Type: application/json" \
  -d '{
    "id": "schema-022",
    "name": "payment_processing",
    "serverId": "db-srv-001",
    "description": "Schema para procesamiento de pagos",
    "owner": "pg_admin",
    "tablesCount": 38,
    "viewsCount": 15,
    "proceduresCount": 22,
    "functionsCount": 18,
    "lastModified": "2024-12-16T19:30:00",
    "sizeInMB": 4200.50
  }'
```

### Buscar schemas de un servidor

```bash
curl -X GET "http://localhost:8080/database-schemas?filter=serverId&search=db-srv-004&page=1&size=20"
```

### Obtener un schema específico

```bash
curl -X GET http://localhost:8080/database-schemas/schema-001
```

### Actualizar un schema

```bash
curl -X PUT http://localhost:8080/database-schemas/schema-022 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "payment_processing_v2",
    "serverId": "db-srv-001",
    "description": "Schema actualizado para procesamiento de pagos v2",
    "owner": "pg_admin",
    "tablesCount": 45,
    "viewsCount": 18,
    "proceduresCount": 28,
    "functionsCount": 22,
    "lastModified": "2024-12-16T20:00:00",
    "sizeInMB": 5100.75
  }'
```

### Eliminar un schema

```bash
curl -X DELETE http://localhost:8080/database-schemas/schema-022
```

---

## Relación con Database Servers

Cada schema debe estar asociado a un servidor de base de datos existente mediante el campo `serverId`. Asegúrate de que el servidor exista antes de crear un schema.

**Ejemplo de flujo:**

1. Crear servidor: `POST /database-servers`
2. Crear schema en ese servidor: `POST /database-schemas` (con `serverId` del paso 1)
3. Consultar schemas de un servidor: `GET /database-schemas?filter=serverId&search={serverId}`

---

## Notas

- El ID del schema es proporcionado por el cliente al momento de la creación.
- Las búsquedas son case-insensitive y soportan coincidencias parciales.
- La paginación utiliza numeración desde 1 (la primera página es 1, no 0).
- Los contadores (tablesCount, viewsCount, etc.) deben ser valores no negativos.
- El tamaño (sizeInMB) debe ser un valor no negativo.
- Los campos `createdAt` y `updatedAt` son gestionados automáticamente por el sistema.
- El campo `lastModified` puede ser actualizado manualmente para reflejar cambios en el schema de la base de datos real.

---

## Métricas del Schema

Los siguientes campos permiten monitorear la complejidad y tamaño de cada schema:

- **tablesCount**: Número de tablas en el schema
- **viewsCount**: Número de vistas
- **proceduresCount**: Número de procedimientos almacenados
- **functionsCount**: Número de funciones
- **sizeInMB**: Tamaño total en megabytes
- **lastModified**: Última vez que se modificó el schema

Estas métricas son útiles para:
- Monitorear el crecimiento de los schemas
- Identificar schemas grandes que requieren optimización
- Planificar estrategias de particionamiento o archivado
- Gestionar la capacidad de almacenamiento

