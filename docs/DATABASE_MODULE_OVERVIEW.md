# Módulo de Bases de Datos - Resumen Completo

## Descripción General
El módulo de bases de datos proporciona una API REST completa para gestionar la infraestructura de bases de datos de la organización, incluyendo servidores, schemas y objetos de base de datos.

## Jerarquía del Modelo

```
DatabaseServer (Servidor de Base de Datos)
    ├── host: string
    ├── port: number
    ├── engine: string (MySQL, PostgreSQL, Oracle, etc.)
    └── schemas: List<DatabaseSchema>
            ├── name: string
            ├── owner: string
            ├── description: string
            └── objects: List<DatabaseObject>
                    ├── name: string
                    ├── type: string (TABLE, VIEW, STORED_PROCEDURE, etc.)
                    ├── rowCount: number
                    └── sizeInKB: number
```

## APIs Implementadas

### 1. Database Servers API
**Base URL**: `/database-servers`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/database-servers` | Buscar servidores con paginación y filtros |
| GET | `/database-servers/{id}` | Obtener servidor por ID |
| POST | `/database-servers` | Crear nuevo servidor |
| PUT | `/database-servers/{id}` | Actualizar servidor |
| DELETE | `/database-servers/{id}` | Eliminar servidor |

**Filtros disponibles**: `host`, `engine`, `environment`, `owner`, `description`

📄 Documentación: `docs/DATABASE_SERVERS_API.md`

### 2. Database Schemas API
**Base URL**: `/database-schemas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/database-schemas` | Buscar schemas con paginación y filtros |
| GET | `/database-schemas/{id}` | Obtener schema por ID |
| POST | `/database-schemas` | Crear nuevo schema |
| PUT | `/database-schemas/{id}` | Actualizar schema |
| DELETE | `/database-schemas/{id}` | Eliminar schema |

**Filtros disponibles**: `name`, `serverId`, `owner`, `description`

📄 Documentación: `docs/DATABASE_SCHEMAS_API.md`

### 3. Database Objects API
**Base URL**: `/database-objects`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/database-objects` | Buscar objetos con paginación y filtros |
| GET | `/database-objects/{id}` | Obtener objeto por ID |
| POST | `/database-objects` | Crear nuevo objeto |
| PUT | `/database-objects/{id}` | Actualizar objeto |
| DELETE | `/database-objects/{id}` | Eliminar objeto |

**Filtros disponibles**: `name`, `type`, `schemaId`, `owner`, `description`

📄 Documentación: `docs/DATABASE_OBJECTS_API.md`

## Tipos de Objetos de Base de Datos

| Tipo | Descripción |
|------|-------------|
| `TABLE` | Tabla de base de datos |
| `VIEW` | Vista |
| `STORED_PROCEDURE` | Procedimiento almacenado |
| `FUNCTION` | Función |
| `TRIGGER` | Trigger/Disparador |
| `INDEX` | Índice |
| `SEQUENCE` | Secuencia |

## Motores de Base de Datos Soportados

- MySQL
- PostgreSQL
- Oracle
- SQL Server
- MariaDB
- MongoDB
- Redis
- Cassandra

## Ambientes

- `development` - Desarrollo
- `testing` - Pruebas
- `staging` - Pre-producción
- `production` - Producción

## Formato de Respuesta Estándar

Todas las APIs de búsqueda retornan el mismo formato:

```json
{
  "data": [
    { /* objetos */ }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 100,
    "totalPages": 5
  },
  "metadata": {
    "filters": ["field1", "field2", "field3"]
  }
}
```

## Características Comunes

### Paginación
- Basada en 1 (la primera página es 1, no 0)
- Parámetros: `page`, `size`, `sortBy`, `sortDir`
- Valores por defecto: `page=1`, `size=20`, `sortDir=asc`

### Búsqueda
- Búsqueda parcial (LIKE)
- Case-insensitive
- Dos modos:
  - Con filtro: busca en un campo específico
  - Sin filtro: busca en todos los campos disponibles

### Ordenamiento
- Por cualquier campo
- Ascendente (`asc`) o descendente (`desc`)

### Validaciones
- Campos requeridos validados
- Tamaños máximos definidos
- Valores numéricos con mínimos
- Mensajes de error en español

## Scripts de Prueba

```bash
# Probar API de servidores
./test-database-servers-api.sh

# Probar API de schemas
./test-database-schemas-api.sh

# Probar API de objetos
./test-database-objects-api.sh
```

## Datos de Ejemplo

```bash
# Cargar datos de ejemplo en MySQL
mysql -u usuario -p base_datos < database-servers-sample-data.sql
mysql -u usuario -p base_datos < database-schemas-sample-data.sql
mysql -u usuario -p base_datos < database-objects-sample-data.sql
```

## Ejemplos de Uso Completos

### 1. Crear un servidor de base de datos
```bash
curl -X POST http://localhost:8080/database-servers \
  -H "Content-Type: application/json" \
  -d '{
    "id": "dbsrv-001",
    "name": "Servidor Principal MySQL",
    "host": "mysql.example.com",
    "port": 3306,
    "engine": "MySQL",
    "version": "8.0.32",
    "environment": "production",
    "owner": "DBA Team",
    "description": "Servidor principal de producción"
  }'
```

### 2. Crear un schema en ese servidor
```bash
curl -X POST http://localhost:8080/database-schemas \
  -H "Content-Type: application/json" \
  -d '{
    "id": "dbs-001",
    "name": "users_db",
    "serverId": "dbsrv-001",
    "owner": "admin",
    "description": "Base de datos de usuarios",
    "tablesCount": 15,
    "viewsCount": 5,
    "proceduresCount": 10,
    "functionsCount": 8,
    "sizeInMB": 1024.5
  }'
```

### 3. Crear objetos en ese schema
```bash
curl -X POST http://localhost:8080/database-objects \
  -H "Content-Type: application/json" \
  -d '{
    "id": "dbo-001",
    "name": "users",
    "type": "TABLE",
    "schemaId": "dbs-001",
    "owner": "admin",
    "description": "Tabla de usuarios",
    "rowCount": 15000,
    "sizeInKB": 2048.5
  }'
```

### 4. Buscar todos los objetos de tipo TABLE en un schema
```bash
curl "http://localhost:8080/database-objects?filter=schemaId&search=dbs-001" | \
  jq '.data[] | select(.type == "TABLE")'
```

### 5. Obtener estadísticas de un servidor
```bash
# Obtener servidor
curl http://localhost:8080/database-servers/dbsrv-001

# Obtener todos sus schemas
curl "http://localhost:8080/database-schemas?filter=serverId&search=dbsrv-001"

# Obtener objetos de un schema específico
curl "http://localhost:8080/database-objects?filter=schemaId&search=dbs-001"
```

## Estructura de Archivos

```
src/main/java/com/orioncode/backoffice/databases/
├── servers/
│   ├── controller/DatabaseServerController.java
│   ├── dto/
│   ├── entity/DatabaseServer.java
│   ├── repository/DatabaseServerRepository.java
│   └── service/DatabaseServerService.java
├── schemas/
│   ├── controller/DatabaseSchemaController.java
│   ├── dto/
│   ├── entity/DatabaseSchema.java
│   ├── repository/DatabaseSchemaRepository.java
│   └── service/DatabaseSchemaService.java
└── objects/
    ├── controller/DatabaseObjectController.java
    ├── dto/
    ├── entity/DatabaseObject.java
    ├── repository/DatabaseObjectRepository.java
    └── service/DatabaseObjectService.java
```

## Manejo de Errores

### 404 Not Found
Cuando el recurso no existe:
```json
{
  "message": "Recurso no encontrado con ID: xxx"
}
```

### 400 Bad Request
Cuando los datos son inválidos:
```json
{
  "errors": [
    {
      "field": "name",
      "message": "El nombre es requerido"
    }
  ]
}
```

### 500 Internal Server Error
Errores del servidor:
```json
{
  "message": "Error interno del servidor",
  "timestamp": "2025-12-17T10:00:00Z"
}
```

## Swagger UI
Toda la documentación interactiva está disponible en:
```
http://localhost:8080/swagger-ui.html
```

## Testing

### Pruebas Unitarias
```bash
mvn test
```

### Pruebas de Integración
```bash
# Iniciar el servidor
./run-dev.sh

# Ejecutar pruebas
./test-database-servers-api.sh
./test-database-schemas-api.sh
./test-database-objects-api.sh
```

## Próximas Mejoras

- [ ] Endpoint de jerarquía completa (servidor → schemas → objetos)
- [ ] Estadísticas agregadas por servidor
- [ ] Endpoint de búsqueda global
- [ ] Exportación de configuraciones
- [ ] Importación masiva de datos
- [ ] Validación de conectividad a servidores
- [ ] Monitoreo de rendimiento
- [ ] Alertas de capacidad

## Contacto y Soporte

Para preguntas o problemas, consultar:
- 📚 Documentación completa en `/docs`
- 🧪 Scripts de prueba en la raíz del proyecto
- 📊 Datos de ejemplo en archivos `.sql`

