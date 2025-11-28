# Endpoint Batch para Tipos de Proyecto

## Descripción

El endpoint `POST /project-types/batch` permite obtener los detalles de múltiples tipos de proyecto en una sola petición HTTP, proporcionando una lista de IDs.

## ¿Por qué POST en lugar de GET?

Aunque el propósito del endpoint es **obtener** (leer) recursos, se utiliza POST por las siguientes razones técnicas:

1. **Sin límites de longitud**: Las URLs GET tienen límite de ~2000 caracteres. Con UUIDs (36 caracteres cada uno), solo cabrían ~50 IDs. POST body puede manejar miles de IDs sin problemas.

2. **Seguridad**: Los IDs en URLs GET quedan registrados en logs del servidor, proxies y historial del navegador. POST body es más discreto.

3. **Mejor experiencia de desarrollo**: Un array JSON es más limpio y fácil de construir que query strings con delimitadores.

4. **Estándar en la industria**: APIs populares como GitHub, Elasticsearch y Google Cloud usan POST para operaciones batch de lectura.

---

## Endpoint

```
POST /project-types/batch
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
  "pt-001",
  "pt-002",
  "pt-003"
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
    "id": "pt-001",
    "name": "Aplicación Web",
    "description": "Proyectos de desarrollo de aplicaciones web",
    "createdAt": "2023-11-10T09:15:00Z",
    "updatedAt": "2024-05-20T14:30:00Z"
  },
  {
    "id": "pt-002",
    "name": "Aplicación Móvil",
    "description": "Proyectos de desarrollo de aplicaciones móviles nativas o híbridas",
    "createdAt": "2023-11-12T10:20:00Z",
    "updatedAt": "2024-06-15T16:45:00Z"
  },
  {
    "id": "pt-003",
    "name": "Microservicio",
    "description": "Proyectos de desarrollo de microservicios y APIs",
    "createdAt": "2023-11-15T11:30:00Z",
    "updatedAt": "2024-07-01T09:00:00Z"
  }
]
```

### Casos especiales

#### IDs inválidos (son ignorados)
**Request:**
```json
["pt-001", "invalid-id", "pt-002"]
```

**Response:** Solo retorna los tipos de proyecto que existen
```json
[
  {
    "id": "pt-001",
    "name": "Aplicación Web",
    ...
  },
  {
    "id": "pt-002",
    "name": "Aplicación Móvil",
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
curl -X POST "http://localhost:8080/project-types/batch" \
  -H "Content-Type: application/json" \
  -d '["pt-001", "pt-002", "pt-003"]'
```

### JavaScript (Fetch API)
```javascript
const projectTypeIds = ['pt-001', 'pt-002', 'pt-003'];

const response = await fetch('http://localhost:8080/project-types/batch', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(projectTypeIds)
});

const projectTypes = await response.json();
console.log(projectTypes);
```

### JavaScript (Axios)
```javascript
const projectTypeIds = ['pt-001', 'pt-002', 'pt-003'];

const response = await axios.post(
  'http://localhost:8080/project-types/batch',
  projectTypeIds
);

console.log(response.data);
```

### Java (Spring RestTemplate)
```java
RestTemplate restTemplate = new RestTemplate();
List<String> ids = Arrays.asList("pt-001", "pt-002", "pt-003");

ResponseEntity<List<ProjectTypeResponseDTO>> response = restTemplate.exchange(
    "http://localhost:8080/project-types/batch",
    HttpMethod.POST,
    new HttpEntity<>(ids),
    new ParameterizedTypeReference<List<ProjectTypeResponseDTO>>() {}
);

List<ProjectTypeResponseDTO> projectTypes = response.getBody();
```

---

## Casos de uso comunes

### 1. Obtener detalles de tipos de proyecto seleccionados en UI
Cuando el usuario selecciona múltiples tipos de proyecto en una tabla y necesitas mostrar sus detalles completos.

### 2. Hidratación de datos de proyectos
Tienes múltiples proyectos y cada uno tiene un tipo. Puedes obtener todos los tipos únicos en una sola petición para enriquecer la información.

```javascript
// 1. Obtener proyectos
const projects = await fetch('/projects?page=1&size=50').then(r => r.json());

// 2. Extraer IDs únicos de tipos de proyecto
const typeIds = [...new Set(projects.data.map(p => p.typeId))];

// 3. Obtener detalles completos de todos los tipos en una sola petición
const projectTypes = await fetch('/project-types/batch', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(typeIds)
}).then(r => r.json());

// 4. Crear un mapa para fácil acceso
const typesMap = Object.fromEntries(projectTypes.map(t => [t.id, t]));

// 5. Enriquecer proyectos con información completa del tipo
const enrichedProjects = projects.data.map(project => ({
  ...project,
  typeDetails: typesMap[project.typeId]
}));
```

### 3. Sincronización de datos
Verificar si ciertos tipos de proyecto aún existen y obtener su información actualizada.

### 4. Exportación de datos
Obtener información completa de múltiples tipos de proyecto para exportar a Excel, PDF, etc.

### 5. Formularios de selección múltiple
Cargar información detallada de tipos de proyecto previamente seleccionados por el usuario.

---

## Comparación con otros endpoints

| Endpoint | Método | Propósito | Límite |
|----------|--------|-----------|--------|
| `/project-types/{id}` | GET | Obtener UN tipo de proyecto | 1 tipo |
| `/project-types` | GET | Buscar con filtros y paginación | 20-100 por página |
| `/project-types/batch` | POST | Obtener MÚLTIPLES tipos específicos | Miles de IDs |
| `/project-types/all` | GET | Obtener todos sin paginación | Todos los registros |

---

## Performance

- **Optimizado para consultas por IDs**: Utiliza `findAllById()` que genera un query SQL con `IN (id1, id2, ...)`
- **Sin límite artificial**: Puede manejar cientos o miles de IDs en una sola petición
- **Lectura de base de datos**: Una sola query SQL independientemente de la cantidad de IDs

### Query SQL generado (ejemplo con 3 IDs)
```sql
SELECT * FROM project_types 
WHERE id IN ('pt-001', 'pt-002', 'pt-003');
```

---

## Testing

Ejecuta el script de prueba incluido:

```bash
./test-project-types-batch-api.sh
```

Este script prueba varios escenarios:
1. Obtener 3 tipos de proyecto válidos
2. Mezcla de IDs válidos e inválidos
3. Array vacío
4. Obtener 10 tipos de proyecto en una sola petición

---

## Relación con otros módulos

Los tipos de proyecto obtenidos por este endpoint pueden ser utilizados para:

- **Proyectos**: Enriquecer información de múltiples proyectos con detalles de sus tipos
- **Reportes**: Generar reportes agrupados por tipo de proyecto
- **Dashboard**: Mostrar estadísticas por tipo de proyecto
- **Formularios**: Pre-cargar información de tipos seleccionados previamente

---

## Notas adicionales

- **Transaccional**: El método usa `@Transactional(readOnly = true)` para optimización
- **Idempotente**: Múltiples llamadas con los mismos IDs retornan el mismo resultado
- **Sin efectos secundarios**: Es una operación de solo lectura
- **Thread-safe**: Puede ser llamado concurrentemente sin problemas
- **UUID como ID**: Los IDs son UUIDs proporcionados por el cliente, no autogenerados

