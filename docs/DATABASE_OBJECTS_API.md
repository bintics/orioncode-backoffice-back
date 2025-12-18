# Database Objects CRUD - Documentación

## Descripción
API para la gestión de objetos de base de datos (tablas, vistas, procedimientos almacenados, funciones, etc.).

## Endpoints

### 1. Buscar Objetos de Base de Datos
**GET** `/database-objects`

Busca objetos de base de datos con filtro dinámico y paginación.

**Parámetros de consulta:**
- `filter` (opcional): Campo sobre el que buscar (name, type, schemaId, owner, description)
- `search` (opcional): Valor a buscar (búsqueda parcial, case-insensitive)
- `page` (opcional, default: 1): Número de página (inicia en 1)
- `size` (opcional, default: 20): Tamaño de página
- `sortBy` (opcional, default: "name"): Campo para ordenar
- `sortDir` (opcional, default: "asc"): Dirección de ordenamiento (asc/desc)

**Ejemplo:**
```bash
GET /database-objects?filter=type&search=TABLE&page=1&size=10
```

**Respuesta:**
```json
{
  "data": [
    {
      "id": "dbo-001",
      "name": "users",
      "type": "TABLE",
      "schemaId": "schema-001",
      "owner": "admin",
      "description": "Tabla de usuarios del sistema",
      "rowCount": 15000,
      "sizeInKB": 2048.5,
      "createdAt": "2025-12-17T10:00:00Z",
      "updatedAt": "2025-12-17T10:00:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 10,
    "totalItems": 1,
    "totalPages": 1
  },
  "metadata": {
    "filters": ["name", "type", "schemaId", "owner", "description"]
  }
}
```

### 2. Obtener Objeto por ID
**GET** `/database-objects/{id}`

Retorna un objeto de base de datos específico por su ID.

**Ejemplo:**
```bash
GET /database-objects/dbo-001
```

**Respuesta:**
```json
{
  "id": "dbo-001",
  "name": "users",
  "type": "TABLE",
  "schemaId": "schema-001",
  "owner": "admin",
  "description": "Tabla de usuarios del sistema",
  "rowCount": 15000,
  "sizeInKB": 2048.5,
  "createdAt": "2025-12-17T10:00:00Z",
  "updatedAt": "2025-12-17T10:00:00Z"
}
```

### 3. Crear Objeto de Base de Datos
**POST** `/database-objects`

Crea un nuevo objeto de base de datos.

**Body:**
```json
{
  "id": "dbo-001",
  "name": "users",
  "type": "TABLE",
  "schemaId": "schema-001",
  "owner": "admin",
  "description": "Tabla de usuarios del sistema",
  "rowCount": 15000,
  "sizeInKB": 2048.5
}
```

**Respuesta:** `201 Created`
```json
{
  "id": "dbo-001",
  "name": "users",
  "type": "TABLE",
  "schemaId": "schema-001",
  "owner": "admin",
  "description": "Tabla de usuarios del sistema",
  "rowCount": 15000,
  "sizeInKB": 2048.5,
  "createdAt": "2025-12-17T10:00:00Z",
  "updatedAt": "2025-12-17T10:00:00Z"
}
```

### 4. Actualizar Objeto de Base de Datos
**PUT** `/database-objects/{id}`

Actualiza un objeto de base de datos existente.

**Body:**
```json
{
  "name": "users_updated",
  "type": "TABLE",
  "schemaId": "schema-001",
  "owner": "admin",
  "description": "Tabla de usuarios del sistema actualizada",
  "rowCount": 16000,
  "sizeInKB": 2100.0
}
```

**Respuesta:** `200 OK`
```json
{
  "id": "dbo-001",
  "name": "users_updated",
  "type": "TABLE",
  "schemaId": "schema-001",
  "owner": "admin",
  "description": "Tabla de usuarios del sistema actualizada",
  "rowCount": 16000,
  "sizeInKB": 2100.0,
  "createdAt": "2025-12-17T10:00:00Z",
  "updatedAt": "2025-12-17T11:30:00Z"
}
```

### 5. Eliminar Objeto de Base de Datos
**DELETE** `/database-objects/{id}`

Elimina un objeto de base de datos por su ID.

**Respuesta:** `204 No Content`

## Validaciones

### CreateDatabaseObjectRequestDTO
- `id`: Requerido, no puede estar vacío
- `name`: Requerido, no puede estar vacío
- `type`: Requerido, no puede estar vacío
- `schemaId`: Requerido, no puede estar vacío
- `owner`: Opcional
- `description`: Opcional
- `rowCount`: Opcional, debe ser >= 0 si se proporciona
- `sizeInKB`: Opcional, debe ser >= 0 si se proporciona

### DatabaseObjectRequestDTO
- `name`: Requerido, entre 2 y 200 caracteres
- `type`: Requerido, máximo 50 caracteres
- `schemaId`: Requerido, máximo 100 caracteres
- `owner`: Opcional, máximo 100 caracteres
- `description`: Opcional, máximo 1000 caracteres
- `rowCount`: Opcional, debe ser >= 0 si se proporciona
- `sizeInKB`: Opcional, debe ser >= 0 si se proporciona

## Tipos de Objetos Comunes
- `TABLE`: Tabla
- `VIEW`: Vista
- `STORED_PROCEDURE`: Procedimiento almacenado
- `FUNCTION`: Función
- `TRIGGER`: Trigger
- `INDEX`: Índice
- `SEQUENCE`: Secuencia

## Manejo de Errores

### 404 Not Found
Cuando el objeto de base de datos no existe:
```json
{
  "message": "Objeto de base de datos no encontrado con ID: dbo-001"
}
```

### 400 Bad Request
Cuando los datos de entrada son inválidos:
```json
{
  "errors": [
    {
      "field": "name",
      "message": "El nombre del objeto es requerido"
    }
  ]
}
```

## Pruebas

Ejecutar el script de pruebas:
```bash
./test-database-objects-api.sh
```

Este script ejecuta todas las operaciones CRUD sobre el endpoint.

