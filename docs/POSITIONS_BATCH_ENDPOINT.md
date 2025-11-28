# Endpoint Batch para Puestos

## Descripción

El endpoint `POST /positions/batch` permite obtener los detalles de múltiples puestos en una sola petición HTTP, proporcionando una lista de IDs.

## ¿Por qué POST en lugar de GET?

Aunque el propósito del endpoint es **obtener** (leer) recursos, se utiliza POST por las siguientes razones técnicas:

1. **Sin límites de longitud**: Las URLs GET tienen límite de ~2000 caracteres. Con UUIDs (36 caracteres cada uno), solo cabrían ~50 IDs. POST body puede manejar miles de IDs sin problemas.

2. **Seguridad**: Los IDs en URLs GET quedan registrados en logs del servidor, proxies y historial del navegador. POST body es más discreto.

3. **Mejor experiencia de desarrollo**: Un array JSON es más limpio y fácil de construir que query strings con delimitadores.

4. **Estándar en la industria**: APIs populares como GitHub, Elasticsearch y Google Cloud usan POST para operaciones batch de lectura.

---

## Endpoint

```
POST /positions/batch
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
  "pos-001",
  "pos-002",
  "pos-003"
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
    "id": "pos-001",
    "name": "Desarrollador Backend",
    "description": "Responsable del desarrollo de APIs y servicios backend",
    "createdAt": "2023-11-10T09:15:00Z",
    "updatedAt": "2024-05-20T14:30:00Z"
  },
  {
    "id": "pos-002",
    "name": "Desarrollador Frontend",
    "description": "Responsable de la interfaz de usuario",
    "createdAt": "2023-11-12T10:20:00Z",
    "updatedAt": "2024-06-15T16:45:00Z"
  },
  {
    "id": "pos-003",
    "name": "DevOps Engineer",
    "description": "Gestión de infraestructura y CI/CD",
    "createdAt": "2023-11-15T11:30:00Z",
    "updatedAt": "2024-07-01T09:00:00Z"
  }
]
```

### Casos especiales

#### IDs inválidos (son ignorados)
**Request:**
```json
["pos-001", "invalid-id", "pos-002"]
```

**Response:** Solo retorna los puestos que existen
```json
[
  {
    "id": "pos-001",
    "name": "Desarrollador Backend",
    ...
  },
  {
    "id": "pos-002",
    "name": "Desarrollador Frontend",
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
curl -X POST "http://localhost:8080/positions/batch" \
  -H "Content-Type: application/json" \
  -d '["pos-001", "pos-002", "pos-003"]'
```

### JavaScript (Fetch API)
```javascript
const positionIds = ['pos-001', 'pos-002', 'pos-003'];

const response = await fetch('http://localhost:8080/positions/batch', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(positionIds)
});

const positions = await response.json();
console.log(positions);
```

### JavaScript (Axios)
```javascript
const positionIds = ['pos-001', 'pos-002', 'pos-003'];

const response = await axios.post(
  'http://localhost:8080/positions/batch',
  positionIds
);

console.log(response.data);
```

### Java (Spring RestTemplate)
```java
RestTemplate restTemplate = new RestTemplate();
List<String> ids = Arrays.asList("pos-001", "pos-002", "pos-003");

ResponseEntity<List<PositionResponseDTO>> response = restTemplate.exchange(
    "http://localhost:8080/positions/batch",
    HttpMethod.POST,
    new HttpEntity<>(ids),
    new ParameterizedTypeReference<List<PositionResponseDTO>>() {}
);

List<PositionResponseDTO> positions = response.getBody();
```

---

## Casos de uso comunes

### 1. Obtener detalles de puestos seleccionados en UI
Cuando el usuario selecciona múltiples puestos en una tabla y necesitas mostrar sus detalles completos.

### 2. Hidratación de datos
Tienes IDs de puestos en memoria y necesitas obtener la información completa para mostrarla.

### 3. Sincronización de datos
Verificar si ciertos puestos aún existen y obtener su información actualizada.

### 4. Exportación de datos
Obtener información completa de múltiples puestos para exportar a Excel, PDF, etc.

---

## Comparación con otros endpoints

| Endpoint | Método | Propósito | Límite |
|----------|--------|-----------|--------|
| `/positions/{id}` | GET | Obtener UN puesto | 1 puesto |
| `/positions` | GET | Buscar con filtros y paginación | 20-100 por página |
| `/positions/batch` | POST | Obtener MÚLTIPLES puestos específicos | Miles de IDs |
| `/positions` (con X-dropdown) | GET | Dropdown con máximo 20 | 20 puestos |

---

## Performance

- **Optimizado para consultas por IDs**: Utiliza `findAllById()` que genera un query SQL con `IN (id1, id2, ...)`
- **Sin límite artificial**: Puede manejar cientos o miles de IDs en una sola petición
- **Lectura de base de datos**: Una sola query SQL independientemente de la cantidad de IDs

### Query SQL generado (ejemplo con 3 IDs)
```sql
SELECT * FROM positions 
WHERE id IN ('pos-001', 'pos-002', 'pos-003');
```

---

## Testing

Ejecuta el script de prueba incluido:

```bash
./test-positions-batch-api.sh
```

Este script prueba varios escenarios:
1. Obtener 3 puestos válidos
2. Mezcla de IDs válidos e inválidos
3. Array vacío
4. Obtener 10 puestos en una sola petición

