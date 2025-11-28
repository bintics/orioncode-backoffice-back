# Endpoints Batch - Resumen General

## Descripción

Los endpoints batch permiten obtener múltiples recursos por sus IDs en una sola petición HTTP. Esta funcionalidad está implementada en todos los módulos principales de la API.

## ¿Por qué POST en lugar de GET?

Aunque el propósito es **obtener** (leer) recursos, se utiliza POST por razones técnicas:

1. **Sin límites de URL**: GET tiene límite de ~2000 caracteres, POST body no tiene límite práctico
2. **Seguridad**: POST body no queda en logs como las URLs
3. **Mejor DX**: Arrays JSON son más limpios que query strings
4. **Estándar de la industria**: GitHub, Elasticsearch, Google Cloud usan POST para batch reads

---

## Endpoints Disponibles

### 1. Puestos (Positions)
```
POST /positions/batch
```

**Body:**
```json
["pos-001", "pos-002", "pos-003"]
```

**Response:**
```json
[
  {
    "id": "pos-001",
    "name": "Desarrollador Backend",
    "description": "...",
    "createdAt": "2023-11-10T09:15:00Z",
    "updatedAt": "2024-05-20T14:30:00Z"
  },
  ...
]
```

**Documentación:** [POSITIONS_BATCH_ENDPOINT.md](./POSITIONS_BATCH_ENDPOINT.md)

**Script de prueba:** `./test-positions-batch-api.sh`

---

### 2. Equipos (Teams)
```
POST /teams/batch
```

**Body:**
```json
["team-001", "team-002", "team-003"]
```

**Response:**
```json
[
  {
    "id": "team-001",
    "name": "Equipo Alfa",
    "description": "...",
    "createdAt": "2023-11-10T09:15:00Z",
    "updatedAt": "2024-05-20T14:30:00Z"
  },
  ...
]
```

**Documentación:** [TEAMS_BATCH_ENDPOINT.md](./TEAMS_BATCH_ENDPOINT.md)

**Script de prueba:** `./test-teams-batch-api.sh`

---

### 3. Tipos de Proyecto (Project Types)
```
POST /project-types/batch
```

**Body:**
```json
["pt-001", "pt-002", "pt-003"]
```

**Response:**
```json
[
  {
    "id": "pt-001",
    "name": "Aplicación Web",
    "description": "...",
    "createdAt": "2023-11-10T09:15:00Z",
    "updatedAt": "2024-05-20T14:30:00Z"
  },
  ...
]
```

**Documentación:** [PROJECT_TYPES_BATCH_ENDPOINT.md](./PROJECT_TYPES_BATCH_ENDPOINT.md)

**Script de prueba:** `./test-project-types-batch-api.sh`

---

## Características Comunes

Todos los endpoints batch comparten las siguientes características:

### ✅ Request
- **Método**: POST
- **Content-Type**: application/json
- **Body**: Array de strings (IDs)
- **Validación**: No permite strings vacíos o null

### ✅ Response
- **Status**: 200 OK
- **Body**: Array de objetos
- **IDs inválidos**: Se ignoran silenciosamente
- **Array vacío**: Retorna array vacío

### ✅ Performance
- **Query SQL optimizado**: `SELECT * FROM table WHERE id IN (...)`
- **Una sola query**: Independiente de la cantidad de IDs
- **Transaccional**: `@Transactional(readOnly = true)`

### ✅ Seguridad
- **Idempotente**: Mismos IDs = mismo resultado
- **Sin efectos secundarios**: Solo lectura
- **Thread-safe**: Puede ser llamado concurrentemente

---

## Casos de Uso Comunes

### 1. Hidratación de relaciones

```javascript
// Obtener colaboradores
const collaborators = await fetch('/collaborators?page=1&size=50')
  .then(r => r.json());

// Extraer IDs únicos de posiciones y equipos
const positionIds = [...new Set(collaborators.data.map(c => c.position))];
const teamIds = [...new Set(collaborators.data.map(c => c.team.id))];

// Obtener detalles completos en paralelo
const [positions, teams] = await Promise.all([
  fetch('/positions/batch', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(positionIds)
  }).then(r => r.json()),
  
  fetch('/teams/batch', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(teamIds)
  }).then(r => r.json())
]);

// Crear mapas para fácil acceso
const positionsMap = Object.fromEntries(positions.map(p => [p.id, p]));
const teamsMap = Object.fromEntries(teams.map(t => [t.id, t]));

// Enriquecer colaboradores
const enriched = collaborators.data.map(c => ({
  ...c,
  positionDetails: positionsMap[c.position],
  teamDetails: teamsMap[c.team.id]
}));
```

### 2. Sincronización de datos

```javascript
// Verificar y actualizar información local
const localIds = getLocalProjectTypeIds();
const serverData = await fetch('/project-types/batch', {
  method: 'POST',
  body: JSON.stringify(localIds)
}).then(r => r.json());

// Identificar IDs que ya no existen en el servidor
const existingIds = new Set(serverData.map(pt => pt.id));
const deletedIds = localIds.filter(id => !existingIds.has(id));

// Actualizar información local
updateLocalCache(serverData);
removeDeletedFromLocal(deletedIds);
```

### 3. Exportación de datos

```javascript
// Obtener IDs de los elementos seleccionados
const selectedIds = getSelectedRowIds();

// Obtener información completa
const fullData = await fetch('/positions/batch', {
  method: 'POST',
  body: JSON.stringify(selectedIds)
}).then(r => r.json());

// Exportar a Excel/PDF
exportToExcel(fullData);
```

### 4. Pre-carga de formularios

```javascript
// Usuario editando un proyecto que tiene múltiples tipos asociados
const project = await fetch(`/projects/${projectId}`).then(r => r.json());

// Pre-cargar información de tipos de proyecto
const typeDetails = await fetch('/project-types/batch', {
  method: 'POST',
  body: JSON.stringify(project.associatedTypeIds)
}).then(r => r.json());

// Mostrar formulario con datos completos
renderEditForm(project, typeDetails);
```

---

## Testing

### Ejecutar todos los tests

```bash
# Posiciones
./test-positions-batch-api.sh

# Equipos
./test-teams-batch-api.sh

# Tipos de proyecto
./test-project-types-batch-api.sh
```

### Ejemplo de test manual con cURL

```bash
# Test con múltiples IDs
curl -X POST "http://localhost:8080/positions/batch" \
  -H "Content-Type: application/json" \
  -d '["pos-001", "pos-002", "pos-003"]' \
  | jq .

# Test con IDs inválidos (ignorados)
curl -X POST "http://localhost:8080/teams/batch" \
  -H "Content-Type: application/json" \
  -d '["team-001", "invalid-id", "team-002"]' \
  | jq .

# Test con array vacío
curl -X POST "http://localhost:8080/project-types/batch" \
  -H "Content-Type: application/json" \
  -d '[]' \
  | jq .
```

---

## Límites y Consideraciones

### ✅ Ventajas
- Sin límites de URL
- Una sola query SQL por petición
- Fácil de usar desde el frontend
- Performance predecible

### ⚠️ Consideraciones
- **Tamaño de payload**: Aunque no hay límite técnico, considera la memoria del servidor
- **Recomendación**: Para más de 1000 IDs, considera paginar las peticiones
- **Timeout**: Peticiones muy grandes pueden requerir más tiempo de respuesta

### 💡 Buenas prácticas
```javascript
// Si tienes muchos IDs, divide en chunks
const chunkSize = 500;
const chunks = [];

for (let i = 0; i < ids.length; i += chunkSize) {
  chunks.push(ids.slice(i, i + chunkSize));
}

// Ejecutar en paralelo (con límite de concurrencia)
const results = [];
for (const chunk of chunks) {
  const data = await fetch('/positions/batch', {
    method: 'POST',
    body: JSON.stringify(chunk)
  }).then(r => r.json());
  
  results.push(...data);
}
```

---

## Integración con Frontend

### React Hook personalizado

```javascript
// useResourcesBatch.js
import { useState, useEffect } from 'react';

export function useResourcesBatch(endpoint, ids) {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!ids || ids.length === 0) {
      setData([]);
      setLoading(false);
      return;
    }

    fetch(`${endpoint}/batch`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(ids)
    })
      .then(res => res.json())
      .then(data => {
        setData(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err);
        setLoading(false);
      });
  }, [endpoint, JSON.stringify(ids)]);

  return { data, loading, error };
}

// Uso
function MyComponent() {
  const positionIds = ['pos-001', 'pos-002'];
  const { data: positions, loading } = useResourcesBatch('/positions', positionIds);
  
  if (loading) return <Loading />;
  return <PositionList positions={positions} />;
}
```

---

## Arquitectura

### Patrón implementado

```
Controller (REST) → Service (Business Logic) → Repository (Data Access)
```

### Ejemplo de implementación

```java
// Service Layer
@Transactional(readOnly = true)
public List<PositionResponseDTO> getPositionsByIds(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
        return List.of();
    }
    
    List<Position> positions = positionRepository.findAllById(ids);
    return positions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}

// Controller Layer
@PostMapping("/batch")
public ResponseEntity<List<PositionResponseDTO>> getPositionsByIds(
        @RequestBody @Valid List<@NotBlank String> ids) {
    return ResponseEntity.ok(positionService.getPositionsByIds(ids));
}
```

---

## Documentación OpenAPI

Todos los endpoints están documentados en OpenAPI/Swagger:

```
http://localhost:8080/swagger-ui.html
```

Busca por "batch" para encontrar todos los endpoints batch disponibles.

---

## Roadmap

### Próximas mejoras consideradas:

- [ ] Endpoint batch para Colaboradores
- [ ] Endpoint batch para Proyectos
- [ ] Cache de resultados (Redis)
- [ ] Rate limiting específico para batch
- [ ] Métricas de uso (cuántos IDs por petición, tiempo de respuesta)
- [ ] Compresión de respuesta para grandes volúmenes

