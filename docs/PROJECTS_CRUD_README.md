# API de Gestión de Proyectos

## Descripción

CRUD completo para la gestión de proyectos en el IDP (Internal Developer Platform). Este módulo permite crear, leer, actualizar y eliminar proyectos, además de realizar búsquedas con filtros y paginación.

## Estructura del Proyecto

```
com.orioncode.frontoffice.projectmanagement/
├── controller/
│   └── ProjectController.java
├── dto/
│   ├── ProjectRequestDTO.java
│   ├── ProjectResponse.java
│   └── ProjectSearchResponseDTO.java
├── entity/
│   └── Project.java
├── repository/
│   └── ProjectRepository.java
└── service/
    └── ProjectService.java
```

## Modelo de Datos

### Project Entity

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | ID único autogenerado |
| name | String(200) | Nombre del proyecto |
| description | String(1000) | Descripción del proyecto |
| status | String(50) | Estado del proyecto (ACTIVE, DRAFT, ARCHIVED) |
| ownerId | String(100) | ID del equipo propietario |
| createdAt | LocalDateTime | Fecha de creación |
| updatedAt | LocalDateTime | Fecha de última actualización |

## Endpoints

### 1. Buscar Proyectos con Paginación

**GET** `/projects`

Busca proyectos con filtro dinámico y paginación.

**Query Parameters:**
- `filter` (opcional): Campo de búsqueda (name, description, status, ownerId)
- `search` (opcional): Valor a buscar (búsqueda parcial, case-insensitive)
- `page` (default: 1): Número de página
- `size` (default: 20): Tamaño de página
- `sortBy` (default: name): Campo para ordenar
- `sortDir` (default: asc): Dirección de ordenamiento (asc/desc)

**Ejemplo de Respuesta:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "E-Commerce Platform",
      "description": "Plataforma de comercio electrónico escalable",
      "status": "ACTIVE",
      "ownerId": "team-001",
      "createdAt": "2023-11-10T09:15:00Z",
      "updatedAt": "2024-05-20T14:30:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 30,
    "totalPages": 2
  },
  "metadata": {
    "filters": ["name", "description", "status", "ownerId"]
  }
}
```

**Ejemplos de Uso:**

```bash
# Listar todos los proyectos
curl -X GET "http://localhost:8080/projects?page=1&size=10"

# Buscar por nombre
curl -X GET "http://localhost:8080/projects?filter=name&search=E-Commerce"

# Buscar por estado
curl -X GET "http://localhost:8080/projects?filter=status&search=ACTIVE"

# Buscar por owner
curl -X GET "http://localhost:8080/projects?filter=ownerId&search=team-001"
```

### 2. Obtener Proyecto por ID

**GET** `/projects/{id}`

Retorna un proyecto específico por su ID.

**Ejemplo:**
```bash
curl -X GET "http://localhost:8080/projects/1"
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "E-Commerce Platform",
  "description": "Plataforma de comercio electrónico escalable",
  "status": "ACTIVE",
  "ownerId": "team-001",
  "createdAt": "2023-11-10T09:15:00Z",
  "updatedAt": "2024-05-20T14:30:00Z"
}
```

### 3. Crear Proyecto

**POST** `/projects`

Crea un nuevo proyecto.

**Request Body:**
```json
{
  "name": "Nuevo Proyecto",
  "description": "Descripción del proyecto",
  "status": "DRAFT",
  "ownerId": "team-005"
}
```

**Ejemplo:**
```bash
curl -X POST "http://localhost:8080/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nuevo Proyecto",
    "description": "Descripción del proyecto",
    "status": "DRAFT",
    "ownerId": "team-005"
  }'
```

**Respuesta:** `201 Created`
```json
{
  "id": 31,
  "name": "Nuevo Proyecto",
  "description": "Descripción del proyecto",
  "status": "DRAFT",
  "ownerId": "team-005",
  "createdAt": "2023-11-27T10:30:00Z",
  "updatedAt": "2023-11-27T10:30:00Z"
}
```

### 4. Actualizar Proyecto

**PUT** `/projects/{id}`

Actualiza un proyecto existente.

**Request Body:**
```json
{
  "name": "Proyecto Actualizado",
  "description": "Nueva descripción",
  "status": "ACTIVE",
  "ownerId": "team-002"
}
```

**Ejemplo:**
```bash
curl -X PUT "http://localhost:8080/projects/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Proyecto Actualizado",
    "description": "Nueva descripción",
    "status": "ACTIVE",
    "ownerId": "team-002"
  }'
```

### 5. Eliminar Proyecto

**DELETE** `/projects/{id}`

Elimina un proyecto por su ID.

**Ejemplo:**
```bash
curl -X DELETE "http://localhost:8080/projects/1"
```

**Respuesta:** `204 No Content`

## Validaciones

- `name`: Obligatorio, entre 2 y 200 caracteres
- `description`: Opcional, máximo 1000 caracteres
- `status`: Obligatorio, máximo 50 caracteres
- `type`: Obligatorio, máximo 50 caracteres (ver tipos disponibles en PROJECT_TYPES.md)
- `ownerId`: Obligatorio, máximo 100 caracteres

## Estados del Proyecto

- **ACTIVE**: Proyecto activo en desarrollo
- **DRAFT**: Proyecto en borrador, aún no iniciado
- **ARCHIVED**: Proyecto archivado o finalizado

## Datos de Prueba

El archivo `data.sql` incluye 30 proyectos de ejemplo con diferentes estados y equipos propietarios. Puedes usar estos datos para probar la API.

## Script de Pruebas

Ejecuta el script de pruebas incluido:

```bash
./test-projects-api.sh
```

Este script ejecutará todas las operaciones CRUD y validará las respuestas.

## Integración con Módulos

Este módulo está diseñado para:
- Asociarse con equipos mediante `ownerId` (relación con el módulo `team`)
- Futuras integraciones con colaboradores asignados al proyecto
- Gestión de recursos y ambientes asociados al proyecto

## Tecnologías

- **Spring Boot 3.x**
- **Spring Data JPA** con Specifications para búsquedas dinámicas
- **Lombok** para reducir código boilerplate
- **Jakarta Validation** para validaciones
- **Swagger/OpenAPI** para documentación de API

## Próximas Funcionalidades

- [ ] Asignación de miembros al proyecto
- [ ] Gestión de recursos (repositorios, ambientes)
- [ ] Historial de cambios y auditoría
- [ ] Permisos por rol en el proyecto
- [ ] Dashboard con métricas del proyecto

