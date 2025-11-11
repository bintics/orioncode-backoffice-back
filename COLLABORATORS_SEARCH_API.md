# API de Búsqueda de Colaboradores

## Endpoint Unificado GET /collaborators

Este endpoint permite realizar búsquedas flexibles de colaboradores con soporte de paginación y filtro dinámico.

### URL Base
```
GET /api/collaborators
```

### Parámetros de Query (todos opcionales)

| Parámetro | Tipo | Descripción | Valor por defecto |
|-----------|------|-------------|-------------------|
| `filter` | String | Campo sobre el que buscar: `firstName`, `lastName`, `team`, `position`, `id` | - |
| `search` | String | Valor a buscar (búsqueda parcial, case-insensitive) | - |
| `page` | Integer | Número de página (comienza en 1) | 1 |
| `size` | Integer | Cantidad de elementos por página | 20 |
| `sortBy` | String | Campo por el cual ordenar | firstName |
| `sortDir` | String | Dirección de ordenamiento: `asc` o `desc` | asc |

**Nota**: Si se proporciona `search` sin `filter`, se buscará en todos los campos (firstName, lastName, team, position, id)

### Ejemplos de Uso

#### 1. Obtener todos los colaboradores (primera página, 20 elementos)
```bash
curl -X GET "http://localhost:8080/api/collaborators"
```

#### 2. Buscar por nombre (firstName)
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=firstName&search=maria"
```

#### 3. Buscar por apellido (lastName)
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=lastName&search=garcia"
```

#### 4. Buscar por equipo (team)
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=team&search=backend"
```

#### 5. Buscar por posición (position)
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=position&search=developer"
```

#### 6. Buscar por ID
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=id&search=coll-0001"
```

#### 7. Búsqueda global (sin especificar filter, busca en todos los campos)
```bash
curl -X GET "http://localhost:8080/api/collaborators?search=juan"
```

#### 8. Búsqueda con paginación
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=position&search=senior&page=2&size=10"
```

#### 9. Ordenar por apellido descendente
```bash
curl -X GET "http://localhost:8080/api/collaborators?sortBy=lastName&sortDir=desc"
```

#### 10. Búsqueda completa con todos los parámetros
```bash
curl -X GET "http://localhost:8080/api/collaborators?filter=team&search=desarrollo&page=1&size=20&sortBy=firstName&sortDir=asc"
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
  },
  "metadata": {
    "filters": [
      "firstName",
      "lastName",
      "team",
      "position",
      "id"
    ]
- `metadata`: Objeto con información sobre filtros disponibles
  }
}
```

### Campos de Respuesta

**Objeto raíz:**
**Objeto metadata:**
- `filters`: Array con los nombres de campos disponibles para filtrar (`firstName`, `lastName`, `team`, `position`, `id`)

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

1. **Búsqueda case-insensitive**: Los parámetros `search` y `position` realizan búsquedas sin distinción entre mayúsculas y minúsculas.
2. **Búsqueda parcial**: Los parámetros `search` y `position` encuentran coincidencias parciales (por ejemplo, "developer" encontrará "Software Developer", "Frontend Developer", etc.).
3. **Filtros combinables**: Puedes combinar `team`, `position` y `search` para realizar búsquedas más específicas.
4. **Paginación simplificada**: La respuesta usa una estructura simple y desacoplada, no depende del objeto `Page` de Spring.
5. **Numeración desde 1**: Las páginas se numeran desde 1 (no desde 0), más intuitivo para el frontend.
6. **Filtro exacto vs parcial**: `team` filtra de forma exacta, mientras que `position` y `search` hacen búsqueda parcial.

### Swagger/OpenAPI

La documentación interactiva está disponible en:
4. **Paginación simplificada**: La respuesta usa una estructura simple y desacoplada, no depende del objeto `Page` de Spring.
5. **Numeración desde 1**: Las páginas se numeran desde 1 (no desde 0), más intuitivo para el frontend.
http://localhost:8080/api/swagger-ui.html
```

## Otros Endpoints Disponibles
4. **Paginación simplificada**: La respuesta usa una estructura simple y desacoplada, no depende del objeto `Page` de Spring.
5. **Numeración desde 1**: Las páginas se numeran desde 1 (no desde 0), más intuitivo para el frontend.
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

