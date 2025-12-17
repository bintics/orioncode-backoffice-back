# API de Servidores de Base de Datos

Este documento describe los endpoints disponibles para gestionar servidores de base de datos en el IDP.

## Endpoints Disponibles

### 1. Buscar Servidores (con paginación y filtros)

**Endpoint:** `GET /database-servers`

**Descripción:** Busca servidores de base de datos con soporte para filtros dinámicos y paginación.

**Parámetros de Query:**

| Parámetro | Tipo | Requerido | Default | Descripción |
|-----------|------|-----------|---------|-------------|
| `filter` | String | No | - | Campo sobre el que buscar: `name`, `engine`, `version`, `host`, `environment`, `description` |
| `search` | String | No | - | Valor a buscar (búsqueda parcial, case-insensitive) |
| `page` | Integer | No | 1 | Número de página (inicia en 1) |
| `size` | Integer | No | 20 | Tamaño de página |
| `sortBy` | String | No | name | Campo para ordenar |
| `sortDir` | String | No | asc | Dirección de ordenamiento (`asc`/`desc`) |

**Ejemplo de Solicitud:**

```bash
# Búsqueda simple (busca en todos los campos)
GET /database-servers?search=postgres&page=1&size=20

# Búsqueda con filtro específico
GET /database-servers?filter=engine&search=postgresql&page=1&size=20

# Búsqueda con ordenamiento
GET /database-servers?filter=environment&search=production&sortBy=name&sortDir=desc
```

**Respuesta Exitosa (200 OK):**

```json
{
  "data": [
    {
      "id": "db-srv-001",
      "name": "PostgreSQL Production Server",
      "engine": "postgresql",
      "version": "15.2",
      "host": "db-prod-01.example.com",
      "port": 5432,
      "environment": "production",
      "description": "Servidor principal de producción",
      "active": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-12-16T14:20:00"
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
      "engine",
      "version",
      "host",
      "environment",
      "description"
    ]
  }
}
```

---

### 2. Obtener Servidor por ID

**Endpoint:** `GET /database-servers/{id}`

**Descripción:** Retorna un servidor de base de datos específico por su ID.

**Parámetros de Path:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `id` | String | Sí | ID del servidor |

**Ejemplo de Solicitud:**

```bash
GET /database-servers/db-srv-001
```

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "db-srv-001",
  "name": "PostgreSQL Production Server",
  "engine": "postgresql",
  "version": "15.2",
  "host": "db-prod-01.example.com",
  "port": 5432,
  "environment": "production",
  "description": "Servidor principal de producción",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-12-16T14:20:00"
}
```

**Respuesta de Error (404 Not Found):**

```json
{
  "message": "Servidor de base de datos no encontrado con ID: db-srv-001"
}
```

---

### 3. Crear Servidor

**Endpoint:** `POST /database-servers`

**Descripción:** Crea un nuevo servidor de base de datos.

**Body (JSON):**

```json
{
  "id": "db-srv-002",
  "name": "MySQL Development Server",
  "engine": "mysql",
  "version": "8.0.35",
  "host": "db-dev-01.example.com",
  "port": 3306,
  "environment": "development",
  "description": "Servidor de desarrollo para equipo backend"
}
```

**Campos del Body:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `id` | String | Sí | ID único del servidor (proporcionado por el cliente) |
| `name` | String | Sí | Nombre del servidor |
| `engine` | String | Sí | Motor de base de datos (postgresql, mysql, oracle, informix, sql-server) |
| `version` | String | Sí | Versión del motor |
| `host` | String | Sí | Host o dirección IP del servidor |
| `port` | Integer | Sí | Puerto (1-65535) |
| `environment` | String | Sí | Ambiente (production, staging, qa, development) |
| `description` | String | No | Descripción del servidor |

**Respuesta Exitosa (201 Created):**

```json
{
  "id": "db-srv-002",
  "name": "MySQL Development Server",
  "engine": "mysql",
  "version": "8.0.35",
  "host": "db-dev-01.example.com",
  "port": 3306,
  "environment": "development",
  "description": "Servidor de desarrollo para equipo backend",
  "active": true,
  "createdAt": "2024-12-16T19:15:30",
  "updatedAt": "2024-12-16T19:15:30"
}
```

---

### 4. Actualizar Servidor

**Endpoint:** `PUT /database-servers/{id}`

**Descripción:** Actualiza un servidor de base de datos existente.

**Parámetros de Path:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `id` | String | Sí | ID del servidor |

**Body (JSON):**

```json
{
  "name": "MySQL Development Server (Updated)",
  "engine": "mysql",
  "version": "8.0.36",
  "host": "db-dev-02.example.com",
  "port": 3306,
  "environment": "development",
  "description": "Servidor de desarrollo actualizado",
  "active": true
}
```

**Campos del Body:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `name` | String | Sí | Nombre del servidor (2-200 caracteres) |
| `engine` | String | Sí | Motor de base de datos (máx. 50 caracteres) |
| `version` | String | Sí | Versión del motor (máx. 50 caracteres) |
| `host` | String | Sí | Host o dirección IP (máx. 255 caracteres) |
| `port` | Integer | Sí | Puerto (1-65535) |
| `environment` | String | Sí | Ambiente (máx. 50 caracteres) |
| `description` | String | No | Descripción (máx. 1000 caracteres) |
| `active` | Boolean | No | Estado activo/inactivo |

**Respuesta Exitosa (200 OK):**

```json
{
  "id": "db-srv-002",
  "name": "MySQL Development Server (Updated)",
  "engine": "mysql",
  "version": "8.0.36",
  "host": "db-dev-02.example.com",
  "port": 3306,
  "environment": "development",
  "description": "Servidor de desarrollo actualizado",
  "active": true,
  "createdAt": "2024-12-16T19:15:30",
  "updatedAt": "2024-12-16T19:20:45"
}
```

---

### 5. Eliminar Servidor

**Endpoint:** `DELETE /database-servers/{id}`

**Descripción:** Elimina un servidor de base de datos por su ID.

**Parámetros de Path:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `id` | String | Sí | ID del servidor |

**Ejemplo de Solicitud:**

```bash
DELETE /database-servers/db-srv-002
```

**Respuesta Exitosa (204 No Content):**

Sin contenido en el body.

**Respuesta de Error (404 Not Found):**

```json
{
  "message": "Servidor de base de datos no encontrado con ID: db-srv-002"
}
```

---

## Motores de Base de Datos Soportados

- `postgresql` - PostgreSQL
- `mysql` - MySQL
- `oracle` - Oracle Database
- `informix` - IBM Informix
- `sql-server` - Microsoft SQL Server

## Ambientes

- `production` - Producción
- `staging` - Staging/Pre-producción
- `qa` - Quality Assurance
- `development` - Desarrollo

## Ejemplos de Uso con cURL

### Crear un servidor

```bash
curl -X POST http://localhost:8080/database-servers \
  -H "Content-Type: application/json" \
  -d '{
    "id": "db-srv-003",
    "name": "Oracle QA Server",
    "engine": "oracle",
    "version": "19c",
    "host": "db-qa-01.example.com",
    "port": 1521,
    "environment": "qa",
    "description": "Servidor Oracle para ambiente de QA"
  }'
```

### Buscar servidores de producción

```bash
curl -X GET "http://localhost:8080/database-servers?filter=environment&search=production&page=1&size=20"
```

### Obtener un servidor específico

```bash
curl -X GET http://localhost:8080/database-servers/db-srv-003
```

### Actualizar un servidor

```bash
curl -X PUT http://localhost:8080/database-servers/db-srv-003 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Oracle QA Server (Updated)",
    "engine": "oracle",
    "version": "21c",
    "host": "db-qa-02.example.com",
    "port": 1521,
    "environment": "qa",
    "description": "Servidor Oracle actualizado para QA",
    "active": true
  }'
```

### Eliminar un servidor

```bash
curl -X DELETE http://localhost:8080/database-servers/db-srv-003
```

## Notas

- El ID del servidor es proporcionado por el cliente al momento de la creación.
- Las búsquedas son case-insensitive y soportan coincidencias parciales.
- La paginación utiliza numeración desde 1 (la primera página es 1, no 0).
- El campo `active` se establece automáticamente en `true` al crear un servidor.
- Los campos `createdAt` y `updatedAt` son gestionados automáticamente por el sistema.

