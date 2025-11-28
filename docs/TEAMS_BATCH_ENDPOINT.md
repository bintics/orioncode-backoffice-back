# Endpoint Batch para Equipos

## Descripción

El endpoint `POST /teams/batch` permite obtener los detalles de múltiples equipos en una sola petición HTTP, proporcionando una lista de IDs.

## ¿Por qué POST en lugar de GET?

Aunque el propósito del endpoint es **obtener** (leer) recursos, se utiliza POST por las siguientes razones técnicas:

1. **Sin límites de longitud**: Las URLs GET tienen límite de ~2000 caracteres. Con UUIDs (36 caracteres cada uno), solo cabrían ~50 IDs. POST body puede manejar miles de IDs sin problemas.

2. **Seguridad**: Los IDs en URLs GET quedan registrados en logs del servidor, proxies y historial del navegador. POST body es más discreto.

3. **Mejor experiencia de desarrollo**: Un array JSON es más limpio y fácil de construir que query strings con delimitadores.

4. **Estándar en la industria**: APIs populares como GitHub, Elasticsearch y Google Cloud usan POST para operaciones batch de lectura.

---

## Endpoint

```
POST /teams/batch
```

---

## Request

### Headers
```
Content-Type: application/json
```

### Body (JSON)
```json
[
  "team-001",
  "team-002",
  "team-003"
]
```

**Validación:**
- El array no puede contener strings vacíos
- Los IDs que no existan serán ignorados silenciosamente
- Si el array está vacío, retorna un array vacío

---

## Response

### Success (200 OK)
```json
[
  {
    "id": "team-001",
    "name": "Equipo Alfa",
    "description": "Equipo de desarrollo frontend",
    "createdAt": "2023-11-10T09:15:00Z",
    "updatedAt": "2024-05-20T14:30:00Z"
  },
  {
    "id": "team-002",
    "name": "Equipo Beta",
    "description": "Equipo de desarrollo backend",
    "createdAt": "2023-11-12T10:20:00Z",
    "updatedAt": "2024-06-15T16:45:00Z"
  },
  {
    "id": "team-003",
    "name": "Equipo Gamma",
    "description": "Equipo de DevOps",
    "createdAt": "2023-11-15T11:30:00Z",
    "updatedAt": "2024-07-01T09:00:00Z"
  }
]
```

### Casos especiales

#### IDs inválidos (son ignorados)
**Request:**
```json
["team-001", "invalid-id", "team-002"]
```

**Response:** Solo retorna los equipos que existen
```json
[
  {
    "id": "team-001",
    "name": "Equipo Alfa",
    ...
  },
  {
    "id": "team-002",
    "name": "Equipo Beta",
    ...
  }
]
```

#### Array vacío
**Request:**
```json
[]
```

**Response:**
```json
[]
```

---

## Ejemplos de uso

### cURL
```bash
curl -X POST "http://localhost:8080/teams/batch" \
  -H "Content-Type: application/json" \
  -d '["team-001", "team-002", "team-003"]'
```

### JavaScript (Fetch API)
```javascript
const teamIds = ['team-001', 'team-002', 'team-003'];

const response = await fetch('http://localhost:8080/teams/batch', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(teamIds)
});

const teams = await response.json();
console.log(teams);
```

### JavaScript (Axios)
```javascript
const teamIds = ['team-001', 'team-002', 'team-003'];

const response = await axios.post(
  'http://localhost:8080/teams/batch',
  teamIds
);

console.log(response.data);
```

### Java (Spring RestTemplate)
```java
RestTemplate restTemplate = new RestTemplate();
List<String> ids = Arrays.asList("team-001", "team-002", "team-003");

ResponseEntity<List<TeamResponseDTO>> response = restTemplate.exchange(
    "http://localhost:8080/teams/batch",
    HttpMethod.POST,
    new HttpEntity<>(ids),
    new ParameterizedTypeReference<List<TeamResponseDTO>>() {}
);

List<TeamResponseDTO> teams = response.getBody();
```

---

## Casos de uso comunes

### 1. Obtener detalles de equipos seleccionados en UI
Cuando el usuario selecciona múltiples equipos en una tabla y necesitas mostrar sus detalles completos.

### 2. Hidratación de datos
Tienes IDs de equipos en memoria y necesitas obtener la información completa para mostrarla.

### 3. Sincronización de datos
Verificar si ciertos equipos aún existen y obtener su información actualizada.

### 4. Exportación de datos
Obtener información completa de múltiples equipos para exportar a Excel, PDF, etc.

### 5. Resolver relaciones
Un colaborador pertenece a un equipo. Al obtener múltiples colaboradores, puedes usar este endpoint para obtener los detalles de todos los equipos únicos en una sola petición.

---

## Comparación con otros endpoints

| Endpoint | Método | Propósito | Límite |
|----------|--------|-----------|--------|
| `/teams/{id}` | GET | Obtener UN equipo | 1 equipo |
| `/teams` | GET | Buscar con filtros y paginación | 20-100 por página |
| `/teams/batch` | POST | Obtener MÚLTIPLES equipos específicos | Miles de IDs |
| `/teams` (con X-dropdown) | GET | Dropdown con máximo 20 | 20 equipos |

---

## Performance

- **Optimizado para consultas por IDs**: Utiliza `findAllById()` que genera un query SQL con `IN (id1, id2, ...)`
- **Sin límite artificial**: Puede manejar cientos o miles de IDs en una sola petición
- **Lectura de base de datos**: Una sola query SQL independientemente de la cantidad de IDs

### Query SQL generado (ejemplo con 3 IDs)
```sql
SELECT * FROM teams 
WHERE id IN ('team-001', 'team-002', 'team-003');
```

---

## Testing

Ejecuta el script de prueba incluido:

```bash
./test-teams-batch-api.sh
```

Este script prueba varios escenarios:
1. Obtener 3 equipos válidos
2. Mezcla de IDs válidos e inválidos
3. Array vacío
4. Obtener 10 equipos en una sola petición

---

## Integración con otros módulos

### Ejemplo: Obtener equipos de múltiples colaboradores

```javascript
// 1. Obtener colaboradores
const collaborators = await fetch('/collaborators?page=1&size=50').then(r => r.json());

// 2. Extraer IDs únicos de equipos
const teamIds = [...new Set(collaborators.data.map(c => c.team.id))];

// 3. Obtener detalles completos de todos los equipos en una sola petición
const teams = await fetch('/teams/batch', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(teamIds)
}).then(r => r.json());

// 4. Crear un mapa para fácil acceso
const teamsMap = Object.fromEntries(teams.map(t => [t.id, t]));

// 5. Enriquecer colaboradores con información completa del equipo
const enrichedCollaborators = collaborators.data.map(collab => ({
  ...collab,
  teamDetails: teamsMap[collab.team.id]
}));
```

---

## Notas adicionales

- **Transaccional**: El método usa `@Transactional(readOnly = true)` para optimización
- **Idempotente**: Múltiples llamadas con los mismos IDs retornan el mismo resultado
- **Sin efectos secundarios**: Es una operación de solo lectura
- **Thread-safe**: Puede ser llamado concurrentemente sin problemas

