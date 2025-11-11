# API de Búsqueda de Colaboradores

## Endpoint Unificado GET /collaborators

Este endpoint permite realizar búsquedas flexibles de colaboradores con soporte de paginación y filtros opcionales.

### URL Base
```
GET /api/collaborators
```

### Parámetros de Query (todos opcionales)

| Parámetro | Tipo | Descripción | Valor por defecto |
|-----------|------|-------------|-------------------|
| `team` | String | Filtrar por nombre de equipo exacto | - |
| `positionId` | String | Filtrar por ID de posición exacto | - |
| `search` | String | Búsqueda de texto en firstName, lastName e id (case-insensitive, parcial) | - |
| `page` | Integer | Número de página (comienza en 1) | 1 |
| `size` | Integer | Cantidad de elementos por página | 20 |
| `sortBy` | String | Campo por el cual ordenar | firstName |
| `sortDir` | String | Dirección de ordenamiento: `asc` o `desc` | asc |

### Ejemplos de Uso

#### 1. Obtener todos los colaboradores (primera página, 20 elementos)
```bash
curl -X GET "http://localhost:8080/api/collaborators"
```

#### 2. Buscar colaboradores por equipo con paginación
```bash
curl -X GET "http://localhost:8080/api/collaborators?team=Desarrollo%20Backend&page=1&size=10"
```

#### 3. Buscar colaboradores por posición
```bash
curl -X GET "http://localhost:8080/api/collaborators?positionId=pos-001"
```

#### 4. Búsqueda de texto libre (busca en nombre, apellido e ID)
```bash
curl -X GET "http://localhost:8080/api/collaborators?search=maria"
```

#### 5. Combinar múltiples filtros
```bash
curl -X GET "http://localhost:8080/api/collaborators?team=Desarrollo%20Backend&positionId=pos-001&search=juan&page=1&size=10"
```

#### 6. Ordenar por apellido descendente
```bash
curl -X GET "http://localhost:8080/api/collaborators?sortBy=lastName&sortDir=desc"
```

#### 7. Obtener segunda página con 50 elementos ordenados por equipo
```bash
curl -X GET "http://localhost:8080/api/collaborators?page=1&size=50&sortBy=team&sortDir=asc"
```

### Respuesta

La respuesta tiene una estructura simple y desacoplada de Spring:

```json
{
  "data": [
    {
      "id": "coll-0001",
      "firstName": "Juan",
      "lastName": "Pérez",
      "position": "Software Developer",
      "team": {
        "id": "team-001",
        "name": "Desarrollo Backend"
      },
      "tags": ["Java", "Clean Code"],
      "createdAt": "2025-11-10T10:00:00",
      "updatedAt": "2025-11-10T10:00:00"
    }
    // ... más colaboradores
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 100,
    "totalPages": 5
  }
}
```

### Campos de Respuesta

**Objeto raíz:**
- `data`: Array con los colaboradores de la página actual
- `pagination`: Objeto con metadata de paginación

**Objeto pagination:**
- `page`: Número de página actual (inicia en 1)
- `pageSize`: Cantidad de elementos por página
- `totalItems`: Total de elementos que cumplen los criterios de búsqueda
- `totalPages`: Total de páginas disponibles

**Objeto colaborador:**
- `id`: ID único del colaborador
- `firstName`: Nombre
- `lastName`: Apellido
- `position`: Nombre del puesto (string simple)
- `team`: Objeto con id y nombre del equipo (puede ser null)
- `tags`: Array de etiquetas/habilidades
- `createdAt`: Fecha de creación
- `updatedAt`: Fecha de última actualización

### Campos Disponibles para Ordenamiento (`sortBy`)

- `firstName` (por defecto)
- `lastName`
- `team`
- `id`
- `createdAt`
- `updatedAt`

### Notas

1. **Búsqueda case-insensitive**: El parámetro `search` realiza búsquedas sin distinción entre mayúsculas y minúsculas.
2. **Búsqueda parcial**: El parámetro `search` encuentra coincidencias parciales (por ejemplo, "mar" encontrará "María").
3. **Filtros combinables**: Puedes combinar `team`, `positionId` y `search` para realizar búsquedas más específicas.
4. **Paginación eficiente**: La respuesta incluye metadata de paginación para facilitar la navegación entre páginas.

### Swagger/OpenAPI

La documentación interactiva está disponible en:
```
http://localhost:8080/api/swagger-ui.html
```

## Otros Endpoints Disponibles
4. **Paginación simplificada**: La respuesta usa una estructura simple y desacoplada, no depende del objeto `Page` de Spring.
5. **Numeración desde 1**: Las páginas se numeran desde 1 (no desde 0), más intuitivo para el frontend.
### Obtener colaborador por ID
```
GET /api/collaborators/{id}
```

### Crear colaborador
```
POST /api/collaborators
Content-Type: application/json

{
  "id": "coll-new-001",
  "firstName": "Nuevo",
  "lastName": "Colaborador",
  "positionId": "pos-001",
  "team": "Desarrollo Backend",
  "tags": ["Java", "Spring"]
}
```

### Actualizar colaborador
```
PUT /api/collaborators/{id}
Content-Type: application/json

{
  "firstName": "Actualizado",
  "lastName": "Colaborador",
  "positionId": "pos-002",
  "team": "Desarrollo Frontend",
  "tags": ["React", "TypeScript"]
}
```

### Eliminar colaborador
```
DELETE /api/collaborators/{id}
```

