# Quick Start - Database Objects CRUD

## Resumen
Se ha implementado un CRUD completo para gestionar objetos de base de datos (tablas, vistas, procedimientos, funciones, triggers, índices, etc.) siguiendo el mismo patrón de DatabaseSchema.

## Estructura Implementada

```
src/main/java/com/orioncode/backoffice/databases/objects/
├── controller/
│   └── DatabaseObjectController.java       (API REST endpoints)
├── dto/
│   ├── CreateDatabaseObjectRequestDTO.java (DTO para crear)
│   ├── DatabaseObjectRequestDTO.java       (DTO para actualizar)
│   ├── DatabaseObjectResponse.java         (DTO de respuesta)
│   └── DatabaseObjectSearchResponseDTO.java (DTO de búsqueda)
├── entity/
│   └── DatabaseObject.java                 (Entidad JPA)
├── repository/
│   └── DatabaseObjectRepository.java       (Repositorio JPA)
└── service/
    └── DatabaseObjectService.java          (Lógica de negocio)
```

## Endpoints Disponibles

### 1. Búsqueda con paginación y filtros
```bash
GET /database-objects?filter=type&search=TABLE&page=1&size=20&sortBy=name&sortDir=asc
```

### 2. Obtener por ID
```bash
GET /database-objects/dbo-001
```

### 3. Crear nuevo objeto
```bash
POST /database-objects
Content-Type: application/json

{
  "id": "dbo-001",
  "name": "users",
  "type": "TABLE",
  "schemaId": "dbs-001",
  "owner": "admin",
  "description": "Tabla de usuarios del sistema",
  "rowCount": 15000,
  "sizeInKB": 2048.5
}
```

### 4. Actualizar objeto
```bash
PUT /database-objects/dbo-001
Content-Type: application/json

{
  "name": "users_updated",
  "type": "TABLE",
  "schemaId": "dbs-001",
  "owner": "admin",
  "description": "Tabla actualizada",
  "rowCount": 16000,
  "sizeInKB": 2100.0
}
```

### 5. Eliminar objeto
```bash
DELETE /database-objects/dbo-001
```

## Campos de Filtrado Disponibles
- `name` - Nombre del objeto
- `type` - Tipo de objeto (TABLE, VIEW, STORED_PROCEDURE, FUNCTION, TRIGGER, INDEX, SEQUENCE)
- `schemaId` - ID del schema al que pertenece
- `owner` - Propietario del objeto
- `description` - Descripción del objeto

## Tipos de Objetos Soportados
- **TABLE**: Tabla
- **VIEW**: Vista
- **STORED_PROCEDURE**: Procedimiento almacenado
- **FUNCTION**: Función
- **TRIGGER**: Trigger
- **INDEX**: Índice
- **SEQUENCE**: Secuencia

## Datos de Prueba
Se incluye un archivo SQL con 20 objetos de ejemplo:
```bash
# Cargar datos de prueba
mysql -u usuario -p base_datos < database-objects-sample-data.sql
```

## Script de Prueba
Ejecutar el script para probar todos los endpoints:
```bash
# Asegurarse de que el servidor esté corriendo
./run-dev.sh

# En otra terminal, ejecutar las pruebas
./test-database-objects-api.sh
```

## Validaciones Implementadas

### Al Crear (CreateDatabaseObjectRequestDTO)
- ✅ `id` es requerido
- ✅ `name` es requerido
- ✅ `type` es requerido
- ✅ `schemaId` es requerido
- ✅ `rowCount` debe ser >= 0 (si se proporciona)
- ✅ `sizeInKB` debe ser >= 0 (si se proporciona)

### Al Actualizar (DatabaseObjectRequestDTO)
- ✅ `name` es requerido (2-200 caracteres)
- ✅ `type` es requerido (máx 50 caracteres)
- ✅ `schemaId` es requerido (máx 100 caracteres)
- ✅ `owner` máximo 100 caracteres
- ✅ `description` máximo 1000 caracteres
- ✅ `rowCount` debe ser >= 0 (si se proporciona)
- ✅ `sizeInKB` debe ser >= 0 (si se proporciona)

## Respuesta Estándar de Búsqueda
```json
{
  "data": [
    {
      "id": "dbo-001",
      "name": "users",
      "type": "TABLE",
      "schemaId": "dbs-001",
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
    "pageSize": 20,
    "totalItems": 100,
    "totalPages": 5
  },
  "metadata": {
    "filters": ["name", "type", "schemaId", "owner", "description"]
  }
}
```

## Ejemplos de Uso

### Buscar todas las tablas
```bash
curl "http://localhost:8080/database-objects?filter=type&search=TABLE"
```

### Buscar objetos por schema
```bash
curl "http://localhost:8080/database-objects?filter=schemaId&search=dbs-001"
```

### Buscar procedimientos almacenados
```bash
curl "http://localhost:8080/database-objects?filter=type&search=STORED_PROCEDURE"
```

### Buscar por nombre
```bash
curl "http://localhost:8080/database-objects?filter=name&search=user"
```

### Búsqueda global (sin filtro específico)
```bash
curl "http://localhost:8080/database-objects?search=audit&page=1&size=10"
```

## Integración con Jerarquía
La jerarquía completa es:
```
DatabaseServer (servidor de BD)
    └── DatabaseSchema (schema)
        └── DatabaseObject (tabla, vista, procedimiento, etc.)
```

Cada objeto de base de datos está vinculado a un schema mediante `schemaId`.

## Documentación Adicional
- 📄 `docs/DATABASE_OBJECTS_API.md` - Documentación completa del API
- 📄 `docs/DATABASE_OBJECTS_IMPLEMENTATION.md` - Detalles de implementación
- 🧪 `test-database-objects-api.sh` - Script de pruebas
- 📊 `database-objects-sample-data.sql` - Datos de ejemplo

## Próximos Pasos
1. ✅ CRUD de DatabaseServer - Implementado
2. ✅ CRUD de DatabaseSchema - Implementado
3. ✅ CRUD de DatabaseObject - **Implementado (actual)**
4. ⏭️ Endpoint de jerarquía completa (servidor > schema > objetos)
5. ⏭️ Estadísticas y métricas de base de datos

## Verificación
Para verificar que todo funciona correctamente:
```bash
# 1. Compilar el proyecto
mvn clean compile

# 2. Ejecutar el proyecto
./run-dev.sh

# 3. En otra terminal, ejecutar las pruebas
./test-database-objects-api.sh

# 4. Verificar Swagger UI
# Abrir en el navegador: http://localhost:8080/swagger-ui.html
```

