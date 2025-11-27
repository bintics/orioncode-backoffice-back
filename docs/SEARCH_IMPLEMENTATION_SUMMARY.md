# ✅ Búsqueda con Filtros: Positions y Teams

## 📝 Resumen

Se implementó la misma funcionalidad de búsqueda con filtros dinámicos, paginación y metadata para los endpoints de **Positions** (Puestos) y **Teams** (Equipos), replicando exactamente el comportamiento del endpoint de Collaborators.

---

## 🎯 Endpoints Implementados

### 1. Positions (Puestos)
```
GET /api/positions
```

### 2. Teams (Equipos)
```
GET /api/teams
```

---

## 📊 Estructura de Respuesta (Ambos Endpoints)

```json
{
  "data": [...],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 50,
    "totalPages": 3
  },
  "metadata": {
    "filters": [
      "name",
      "description",
      "id"
    ]
  }
}
```

---

## 🔍 Parámetros Disponibles

Ambos endpoints aceptan los mismos parámetros:

| Parámetro | Tipo | Descripción | Default |
|-----------|------|-------------|---------|
| `filter` | String | Campo: `name`, `description`, `id` | - |
| `search` | String | Valor a buscar (parcial, case-insensitive) | - |
| `page` | Integer | Número de página (desde 1) | 1 |
| `size` | Integer | Elementos por página | 20 |
| `sortBy` | String | Campo para ordenar | `name` |
| `sortDir` | String | Dirección: `asc` o `desc` | `asc` |

---

## 💡 Ejemplos de Uso

### Positions (Puestos)

```bash
# Obtener todos los puestos (primera página)
GET /api/positions

# Buscar por nombre
GET /api/positions?filter=name&search=developer

# Buscar por descripción
GET /api/positions?filter=description&search=senior

# Búsqueda global (todos los campos)
GET /api/positions?search=software

# Con paginación
GET /api/positions?filter=name&search=manager&page=1&size=10

# Ordenar por nombre descendente
GET /api/positions?sortBy=name&sortDir=desc
```

### Teams (Equipos)

```bash
# Obtener todos los equipos (primera página)
GET /api/teams

# Buscar por nombre
GET /api/teams?filter=name&search=backend

# Buscar por descripción
GET /api/teams?filter=description&search=desarrollo

# Búsqueda global (todos los campos)
GET /api/teams?search=frontend

# Con paginación
GET /api/teams?filter=name&search=desarrollo&page=1&size=10

# Ordenar por nombre descendente
GET /api/teams?sortBy=name&sortDir=desc
```

---

## 🎨 Ejemplos de Respuesta

### Positions:
```json
{
  "data": [
    {
      "id": "pos-001",
      "name": "Software Developer",
      "description": "Desarrollador de software con experiencia inicial",
      "createdAt": "2025-11-10T10:00:00",
      "updatedAt": "2025-11-10T10:00:00"
    },
    {
      "id": "pos-002",
      "name": "Software Developer SSr",
      "description": "Desarrollador semi-senior con 2-4 años",
      "createdAt": "2025-11-10T10:00:00",
      "updatedAt": "2025-11-10T10:00:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 20,
    "totalPages": 1
  },
  "metadata": {
    "filters": ["name", "description", "id"]
  }
}
```

### Teams:
```json
{
  "data": [
    {
      "id": "team-001",
      "name": "Desarrollo Backend",
      "description": "Equipo dedicado al desarrollo de servicios backend",
      "createdAt": "2025-11-10T10:00:00",
      "updatedAt": "2025-11-10T10:00:00"
    },
    {
      "id": "team-002",
      "name": "Desarrollo Frontend",
      "description": "Equipo enfocado en interfaces de usuario",
      "createdAt": "2025-11-10T10:00:00",
      "updatedAt": "2025-11-10T10:00:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 15,
    "totalPages": 1
  },
  "metadata": {
    "filters": ["name", "description", "id"]
  }
}
```

---

## 🛠️ Archivos Modificados

### Positions:
1. ✅ `PositionRepository.java` - Agregado `JpaSpecificationExecutor`
2. ✅ `PositionService.java` - Agregado método `searchPositions()`
3. ✅ `PositionController.java` - Endpoint GET actualizado con filtros

### Teams:
4. ✅ `TeamRepository.java` - Agregado `JpaSpecificationExecutor`
5. ✅ `TeamService.java` - Agregado método `searchTeams()`
6. ✅ `TeamController.java` - Endpoint GET actualizado con filtros

---

## 🎯 Filtros Disponibles por Endpoint

### Collaborators:
- `firstName`
- `lastName`
- `team`
- `position`
- `id`

### Positions:
- `name`
- `description`
- `id`

### Teams:
- `name`
- `description`
- `id`

---

## 💻 Uso en Frontend

### TypeScript/JavaScript:

```typescript
// Buscar posiciones
async function searchPositions(filter: string, search: string) {
  const params = new URLSearchParams();
  if (filter) params.append('filter', filter);
  if (search) params.append('search', search);
  
  const response = await fetch(`/api/positions?${params}`);
  const data = await response.json();
  
  return {
    positions: data.data,
    pagination: data.pagination,
    availableFilters: data.metadata.filters
  };
}

// Buscar equipos
async function searchTeams(filter: string, search: string) {
  const params = new URLSearchParams();
  if (filter) params.append('filter', filter);
  if (search) params.append('search', search);
  
  const response = await fetch(`/api/teams?${params}`);
  const data = await response.json();
  
  return {
    teams: data.data,
    pagination: data.pagination,
    availableFilters: data.metadata.filters
  };
}
```

### Vue 3 Ejemplo:

```vue
<template>
  <div>
    <!-- Para Positions -->
    <h2>Buscar Puestos</h2>
    <select v-model="positionFilter">
      <option value="">Todos los campos</option>
      <option v-for="f in positionFilters" :value="f">{{ f }}</option>
    </select>
    <input v-model="positionSearch" placeholder="Buscar puesto..." />
    
    <!-- Para Teams -->
    <h2>Buscar Equipos</h2>
    <select v-model="teamFilter">
      <option value="">Todos los campos</option>
      <option v-for="f in teamFilters" :value="f">{{ f }}</option>
    </select>
    <input v-model="teamSearch" placeholder="Buscar equipo..." />
  </div>
</template>

<script setup>
const positionFilters = ref([]);
const teamFilters = ref([]);

async function loadPositions() {
  const response = await fetch('/api/positions');
  const data = await response.json();
  positionFilters.value = data.metadata.filters;
}

async function loadTeams() {
  const response = await fetch('/api/teams');
  const data = await response.json();
  teamFilters.value = data.metadata.filters;
}
</script>
```

---

## 🧪 Testing

### Positions:

```bash
# Test búsqueda por nombre
curl "http://localhost:8080/api/positions?filter=name&search=developer" | jq

# Test búsqueda global
curl "http://localhost:8080/api/positions?search=senior" | jq

# Test paginación
curl "http://localhost:8080/api/positions?page=1&size=5" | jq

# Verificar metadata
curl "http://localhost:8080/api/positions" | jq '.metadata'
```

### Teams:

```bash
# Test búsqueda por nombre
curl "http://localhost:8080/api/teams?filter=name&search=backend" | jq

# Test búsqueda global
curl "http://localhost:8080/api/teams?search=desarrollo" | jq

# Test paginación
curl "http://localhost:8080/api/teams?page=1&size=5" | jq

# Verificar metadata
curl "http://localhost:8080/api/teams" | jq '.metadata'
```

---

## 🎯 Comportamiento de Búsqueda

### Con `filter` + `search`:
```
GET /api/positions?filter=name&search=developer
→ Busca "developer" SOLO en el campo "name"
```

### Solo `search` (sin `filter`):
```
GET /api/positions?search=developer
→ Busca "developer" en TODOS los campos (name, description, id)
```

### Características:
- ✅ **Case-insensitive**: `DEVELOPER` = `developer`
- ✅ **Búsqueda parcial**: `dev` encuentra "Developer", "DevOps"
- ✅ **Paginación desde 1**: Primera página es `page=1`
- ✅ **Metadata incluido**: Lista de filtros disponibles

---

## ✅ Verificaciones

- ✅ Positions compilado sin errores
- ✅ Teams compilado sin errores
- ✅ Ambos endpoints con filtros dinámicos
- ✅ Metadata incluido en respuestas
- ✅ Paginación funcionando
- ✅ Búsqueda global y específica
- ✅ Ordenamiento configurable

---

## 📋 Resumen de Implementación

| Feature | Collaborators | Positions | Teams |
|---------|--------------|-----------|-------|
| Filtro dinámico | ✅ | ✅ | ✅ |
| Búsqueda global | ✅ | ✅ | ✅ |
| Paginación | ✅ | ✅ | ✅ |
| Metadata | ✅ | ✅ | ✅ |
| Ordenamiento | ✅ | ✅ | ✅ |
| Case-insensitive | ✅ | ✅ | ✅ |

---

## 🚀 Estado Final

**TODOS LOS ENDPOINTS IMPLEMENTADOS Y FUNCIONANDO** ✅

Los tres endpoints principales de la aplicación ahora tienen:
- Búsqueda con filtros dinámicos
- Paginación completa
- Metadata con filtros disponibles
- Estructura de respuesta consistente
- Totalmente desacoplados de Spring Page

---

**Fecha**: 10 de Noviembre, 2025  
**Estado**: ✅ COMPLETADO  
**Endpoints listos**: `/collaborators`, `/positions`, `/teams`

