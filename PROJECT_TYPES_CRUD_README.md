# API de Tipos de Proyecto

## Descripción

CRUD completo para la administración de tipos de proyecto. Este módulo permite crear, leer, actualizar y eliminar tipos de proyecto, que luego pueden ser utilizados para categorizar proyectos en el IDP.

## Estructura del Módulo

```
com.orioncode.frontoffice.projectmanagement/
├── controller/
│   └── ProjectTypeController.java
├── dto/
│   ├── ProjectTypeRequestDTO.java
│   ├── ProjectTypeResponseDTO.java
│   └── ProjectTypeSearchResponseDTO.java
├── entity/
│   └── ProjectType.java
├── repository/
│   └── ProjectTypeRepository.java
└── service/
    └── ProjectTypeService.java
```

## Modelo de Datos

### ProjectType Entity

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | ID único autogenerado |
| name | String(100) | Nombre del tipo (único) |
| description | String(500) | Descripción del tipo |
| createdAt | LocalDateTime | Fecha de creación |
| updatedAt | LocalDateTime | Fecha de última actualización |

## Endpoints

### 1. Buscar Tipos de Proyecto con Paginación

**GET** `/project-types`

Busca tipos de proyecto con filtro dinámico y paginación.

**Query Parameters:**
- `filter` (opcional): Campo de búsqueda (name, description)
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
      "name": "WEB_APPLICATION",
      "description": "Aplicaciones web tradicionales, portales, dashboards y sistemas web interactivos",
      "createdAt": "2024-11-27T10:00:00",
      "updatedAt": "2024-11-27T10:00:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 11,
    "totalPages": 1
  },
  "metadata": {
    "filters": ["name", "description"]
  }
}
```

**Ejemplos de Uso:**

```bash
# Listar todos los tipos con paginación
curl -X GET "http://localhost:8080/project-types?page=1&size=10"

# Buscar por nombre
curl -X GET "http://localhost:8080/project-types?filter=name&search=WEB"

# Buscar por descripción
curl -X GET "http://localhost:8080/project-types?filter=description&search=infraestructura"
```

---

### 2. Listar Todos los Tipos sin Paginación

**GET** `/project-types/all`

Retorna todos los tipos de proyecto sin paginación. Útil para dropdowns y selects.

**Ejemplo:**
```bash
curl -X GET "http://localhost:8080/project-types/all"
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "WEB_APPLICATION",
    "description": "Aplicaciones web tradicionales, portales, dashboards y sistemas web interactivos",
    "createdAt": "2024-11-27T10:00:00",
    "updatedAt": "2024-11-27T10:00:00"
  },
  {
    "id": 2,
    "name": "MOBILE_APP",
    "description": "Aplicaciones móviles nativas o híbridas para iOS, Android o multiplataforma",
    "createdAt": "2024-11-27T10:00:00",
    "updatedAt": "2024-11-27T10:00:00"
  }
]
```

---

### 3. Obtener Tipo por ID

**GET** `/project-types/{id}`

Retorna un tipo de proyecto específico por su ID.

**Ejemplo:**
```bash
curl -X GET "http://localhost:8080/project-types/1"
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "WEB_APPLICATION",
  "description": "Aplicaciones web tradicionales, portales, dashboards y sistemas web interactivos",
  "createdAt": "2024-11-27T10:00:00",
  "updatedAt": "2024-11-27T10:00:00"
}
```

---

### 4. Obtener Tipo por Nombre

**GET** `/project-types/name/{name}`

Retorna un tipo de proyecto específico por su nombre.

**Ejemplo:**
```bash
curl -X GET "http://localhost:8080/project-types/name/MOBILE_APP"
```

**Respuesta:**
```json
{
  "id": 2,
  "name": "MOBILE_APP",
  "description": "Aplicaciones móviles nativas o híbridas para iOS, Android o multiplataforma",
  "createdAt": "2024-11-27T10:00:00",
  "updatedAt": "2024-11-27T10:00:00"
}
```

---

### 5. Crear Tipo de Proyecto

**POST** `/project-types`

Crea un nuevo tipo de proyecto.

**Request Body:**
```json
{
  "name": "BLOCKCHAIN",
  "description": "Proyectos relacionados con blockchain y tecnologías distribuidas"
}
```

**Ejemplo:**
```bash
curl -X POST "http://localhost:8080/project-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "BLOCKCHAIN",
    "description": "Proyectos relacionados con blockchain y tecnologías distribuidas"
  }'
```

**Respuesta:** `201 Created`
```json
{
  "id": 12,
  "name": "BLOCKCHAIN",
  "description": "Proyectos relacionados con blockchain y tecnologías distribuidas",
  "createdAt": "2024-11-27T11:30:00",
  "updatedAt": "2024-11-27T11:30:00"
}
```

---

### 6. Actualizar Tipo de Proyecto

**PUT** `/project-types/{id}`

Actualiza un tipo de proyecto existente.

**Request Body:**
```json
{
  "name": "WEB_APPLICATION",
  "description": "Aplicaciones web modernas con tecnologías front y backend actualizadas"
}
```

**Ejemplo:**
```bash
curl -X PUT "http://localhost:8080/project-types/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "WEB_APPLICATION",
    "description": "Aplicaciones web modernas con tecnologías front y backend actualizadas"
  }'
```

---

### 7. Eliminar Tipo de Proyecto

**DELETE** `/project-types/{id}`

Elimina un tipo de proyecto por su ID.

**Ejemplo:**
```bash
curl -X DELETE "http://localhost:8080/project-types/12"
```

**Respuesta:** `204 No Content`

---

## Validaciones

- `name`: Obligatorio, entre 2 y 100 caracteres, debe ser único
- `description`: Opcional, máximo 500 caracteres

## Reglas de Negocio

1. **Nombres Únicos**: No pueden existir dos tipos con el mismo nombre
2. **Nombre Obligatorio**: El campo name es requerido
3. **Validación en Actualización**: Al actualizar, valida que no exista otro tipo con el nuevo nombre

## Tipos Predefinidos

El sistema incluye 11 tipos predefinidos:

| ID | Nombre | Descripción |
|----|--------|-------------|
| 1 | WEB_APPLICATION | Aplicaciones web tradicionales |
| 2 | MOBILE_APP | Aplicaciones móviles |
| 3 | MICROSERVICE | Microservicios independientes |
| 4 | INFRASTRUCTURE | Infraestructura y DevOps |
| 5 | DATA_ENGINEERING | Ingeniería de datos |
| 6 | MACHINE_LEARNING | ML e IA |
| 7 | SECURITY | Seguridad y auditoría |
| 8 | INTEGRATION | Integraciones de sistemas |
| 9 | TOOLING | Herramientas internas |
| 10 | IOT | Internet of Things |
| 11 | ARCHITECTURE | Diseño arquitectónico |

## Integración con Proyectos

Los tipos de proyecto se relacionan con la entidad `Project` a través del campo `type`. Cuando se crea o actualiza un proyecto, se debe proporcionar un nombre de tipo válido.

**Ejemplo de uso en Project:**
```json
{
  "name": "Nueva App Móvil",
  "description": "App para iOS y Android",
  "status": "ACTIVE",
  "type": "MOBILE_APP",
  "ownerId": "team-003"
}
```

## Script de Pruebas

Ejecuta el script de pruebas incluido:

```bash
./test-project-types-api.sh
```

Este script ejecutará todas las operaciones CRUD y validará las respuestas.

## Casos de Uso

### 1. Dropdown de Tipos en Formulario
```bash
# Obtener todos los tipos para un select
GET /project-types/all
```

### 2. Validación de Tipo al Crear Proyecto
```bash
# Verificar que un tipo existe antes de crear proyecto
GET /project-types/name/WEB_APPLICATION
```

### 3. Administración de Catálogo
```bash
# Listar tipos con paginación para administrar
GET /project-types?page=1&size=20&sortBy=name

# Agregar nuevo tipo personalizado
POST /project-types
{
  "name": "CUSTOM_TYPE",
  "description": "Tipo personalizado para proyectos especiales"
}
```

### 4. Búsqueda de Tipos
```bash
# Buscar tipos relacionados con mobile
GET /project-types?search=mobile

# Buscar en descripciones
GET /project-types?filter=description&search=datos
```

## Tabla de Base de Datos

```sql
CREATE TABLE project_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    INDEX idx_name (name)
);
```

## Tecnologías

- **Spring Boot 3.x**
- **Spring Data JPA** con Specifications
- **Lombok** para reducir boilerplate
- **Jakarta Validation** para validaciones
- **Swagger/OpenAPI** para documentación

## Próximas Mejoras

- [ ] Endpoint para estadísticas de uso por tipo
- [ ] Validación de eliminación (no permitir si hay proyectos asociados)
- [ ] Sistema de iconos/colores por tipo
- [ ] Tipos activos/inactivos
- [ ] Auditoría de cambios en tipos

## Notas Importantes

⚠️ **Eliminación de Tipos**: Actualmente se puede eliminar cualquier tipo. En el futuro se agregará validación para evitar eliminar tipos que estén siendo usados por proyectos.

💡 **Convención de Nombres**: Se recomienda usar `SNAKE_CASE` en mayúsculas para nombres de tipos (ej: `WEB_APPLICATION`, `MOBILE_APP`).

🔍 **Búsqueda Inteligente**: La búsqueda sin especificar filtro busca en todos los campos disponibles (name y description).

