# Endpoint Dropdown para Posiciones

## Resumen
El endpoint GET `/positions` tiene dos modos de operación: modo búsqueda completa (con paginación) y modo dropdown (para listas desplegables). El modo se determina mediante un header HTTP.

## Características

### Endpoint
- **URL**: `GET /positions`
- **Header**: `X-dropdown` (opcional)
  - Si está presente: Activa el **modo dropdown**
  - Si no está presente: Activa el **modo búsqueda completa**
  
### Modo Dropdown (con header X-dropdown)
- **Parámetros**:
  - `search` (opcional): Valor a buscar en los campos `name`, `description` e `id`
  
### Comportamiento
1. **Sin parámetro search**: Retorna los primeros 20 puestos ordenados alfabéticamente por nombre
2. **Con parámetro search**: Filtra en los campos `name`, `description` e `id` y retorna máximo 20 resultados que coincidan
3. **Búsqueda**: Case-insensitive y parcial (busca coincidencias en cualquier parte del texto)
4. **Ordenamiento**: Siempre ordenado por nombre ascendente
5. **Sin paginación**: Retorna directamente una lista de objetos, sin estructura de paginación

### Estructura de Respuesta
```json
[
    {
        "id": "pos-001",
        "name": "Desarrollador Backend",
        "description": "Responsable del desarrollo backend",
        "createdAt": "2024-01-15T10:00:00Z",
        "updatedAt": "2024-01-15T10:00:00Z"
    },
    {
        "id": "pos-002",
        "name": "Desarrollador Frontend",
        "description": "Responsable del desarrollo frontend",
        "createdAt": "2024-01-16T10:00:00Z",
        "updatedAt": "2024-01-16T10:00:00Z"
    }
]
```

## Ejemplos de Uso

### Modo Dropdown

#### Obtener los primeros 20 puestos para dropdown
```bash
GET /positions
Headers: X-dropdown: true
```

#### Buscar puestos que contengan "desa" para dropdown
```bash
GET /positions?search=desa
Headers: X-dropdown: true
```
Retorna puestos que contengan "desa" en el nombre, descripción o ID.

#### Buscar por ID específico para dropdown
```bash
GET /positions?search=pos-001
Headers: X-dropdown: true
```

### Modo Búsqueda Completa

#### Buscar con paginación (sin header)
```bash
GET /positions?page=1&size=20&filter=name&search=backend
```

#### Buscar todos los registros paginados
```bash
GET /positions?page=1&size=20
```

## Diferencias entre Modos

| Característica | Modo Dropdown (con header X-dropdown) | Modo Búsqueda Completa (sin header) |
|----------------|--------------------------------------|-------------------------------------|
| Propósito | Llenar listas desplegables | Búsqueda completa con tabla |
| Header requerido | `X-dropdown` (cualquier valor) | No requiere header |
| Paginación | No | Sí (personalizada) |
| Límite de resultados | 20 fijos | Configurable |
| Parámetro de filtro | Solo `search` | `filter` y `search` |
| Estructura respuesta | Array simple | Objeto con data, pagination y metadata |
| Ordenamiento | Fijo (name ASC) | Configurable (sortBy, sortDir) |

## Archivos Modificados

1. **PositionController.java**
   - Agregado endpoint `@GetMapping("/dropdown")`
   - Documentación Swagger con `@Operation`

2. **PositionService.java**
   - Agregado método `getPositionsForDropdown(String search)`
   - Usa `Specification` para búsqueda dinámica
   - Limita a 20 resultados con `PageRequest.of(0, 20, Sort.by("name").ascending())`

## Notas Técnicas
- Un solo endpoint maneja ambos casos: búsqueda completa y dropdown
- El header `X-dropdown` puede tener cualquier valor (true, 1, yes, etc.) - solo se verifica su presencia
- Usa Spring Data JPA Specifications para construir queries dinámicas
- El límite de 20 registros en modo dropdown está hardcodeado para optimizar el rendimiento
- La búsqueda es en múltiples campos simultáneamente (OR)
- Compatible con MySQL

## Ejemplo desde Frontend (JavaScript/TypeScript)

### Para Dropdown
```javascript
// Obtener puestos para un select/dropdown
fetch('/positions?search=backend', {
  headers: {
    'X-dropdown': 'true'
  }
})
.then(response => response.json())
.then(data => {
  // data es un array simple de PositionResponseDTO
  // Ejemplo: [{id: "pos-001", name: "Backend Developer", ...}, ...]
});
```

### Para Tabla con Paginación
```javascript
// Obtener puestos para una tabla paginada
fetch('/positions?page=1&size=20&filter=name&search=backend')
.then(response => response.json())
.then(data => {
  // data tiene estructura: {data: [...], pagination: {...}, metadata: {...}}
  console.log(data.data); // Los registros
  console.log(data.pagination); // Info de paginación
});
```

