# 🎯 Referencia Rápida de APIs - Frontend

## 📍 Endpoints con Búsqueda y Filtros

Los tres endpoints principales tienen la misma estructura de búsqueda:

---

## 1️⃣ Collaborators (Colaboradores)

```
GET /api/collaborators
```

### Filtros disponibles:
- `firstName` - Nombre
- `lastName` - Apellido
- `team` - Equipo
- `position` - Puesto
- `id` - ID

### Ejemplos:
```bash
# Buscar por nombre
GET /api/collaborators?filter=firstName&search=maria

# Buscar por equipo
GET /api/collaborators?filter=team&search=backend

# Búsqueda global
GET /api/collaborators?search=developer
```

---

## 2️⃣ Positions (Puestos)

```
GET /api/positions
```

### Filtros disponibles:
- `name` - Nombre del puesto
- `description` - Descripción
- `id` - ID

### Ejemplos:
```bash
# Buscar por nombre
GET /api/positions?filter=name&search=developer

# Buscar por descripción
GET /api/positions?filter=description&search=senior

# Búsqueda global
GET /api/positions?search=manager
```

---

## 3️⃣ Teams (Equipos)

```
GET /api/teams
```

### Filtros disponibles:
- `name` - Nombre del equipo
- `description` - Descripción
- `id` - ID

### Ejemplos:
```bash
# Buscar por nombre
GET /api/teams?filter=name&search=backend

# Buscar por descripción
GET /api/teams?filter=description&search=desarrollo

# Búsqueda global
GET /api/teams?search=frontend
```

---

## 📊 Parámetros Comunes (Todos los Endpoints)

| Parámetro | Tipo | Descripción | Default |
|-----------|------|-------------|---------|
| `filter` | String | Campo donde buscar | - |
| `search` | String | Valor a buscar | - |
| `page` | Integer | Número de página (desde 1) | 1 |
| `size` | Integer | Elementos por página | 20 |
| `sortBy` | String | Campo para ordenar | Varía por endpoint |
| `sortDir` | String | `asc` o `desc` | `asc` |

---

## 📦 Estructura de Respuesta (Todos)

```typescript
interface PageResponse<T> {
  data: T[];                    // Array de resultados
  pagination: {
    page: number;               // Página actual (desde 1)
    pageSize: number;           // Elementos por página
    totalItems: number;         // Total de elementos
    totalPages: number;         // Total de páginas
  };
  metadata: {
    filters: string[];          // Filtros disponibles
  };
}
```

---

## 💻 Código Reutilizable (TypeScript)

```typescript
// Función genérica para búsqueda
async function searchAPI<T>(
  endpoint: string,
  filter?: string,
  search?: string,
  page: number = 1,
  size: number = 20
): Promise<PageResponse<T>> {
  const params = new URLSearchParams();
  
  if (filter) params.append('filter', filter);
  if (search) params.append('search', search);
  params.append('page', page.toString());
  params.append('size', size.toString());
  
  const response = await fetch(`/api/${endpoint}?${params}`);
  return response.json();
}

// Uso:
const collaborators = await searchAPI('collaborators', 'firstName', 'juan');
const positions = await searchAPI('positions', 'name', 'developer');
const teams = await searchAPI('teams', 'name', 'backend');
```

---

## 🎨 Componente Vue Reutilizable

```vue
<template>
  <div class="search-component">
    <select v-model="selectedFilter">
      <option value="">Todos los campos</option>
      <option v-for="f in availableFilters" :value="f">
        {{ getLabel(f) }}
      </option>
    </select>
    
    <input 
      v-model="searchTerm" 
      @input="onSearchChange"
      :placeholder="`Buscar ${entityName}...`"
    />
    
    <div v-if="loading">Cargando...</div>
    
    <div v-for="item in items" :key="item.id">
      <slot :item="item"></slot>
    </div>
    
    <div class="pagination">
      <button @click="prevPage" :disabled="page === 1">◀</button>
      <span>Página {{ page }} de {{ totalPages }}</span>
      <button @click="nextPage" :disabled="page === totalPages">▶</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

interface Props {
  endpoint: 'collaborators' | 'positions' | 'teams';
  entityName: string;
}

const props = defineProps<Props>();

const items = ref([]);
const availableFilters = ref<string[]>([]);
const selectedFilter = ref('');
const searchTerm = ref('');
const page = ref(1);
const totalPages = ref(1);
const loading = ref(false);

async function search() {
  loading.value = true;
  try {
    const params = new URLSearchParams();
    if (selectedFilter.value) params.append('filter', selectedFilter.value);
    if (searchTerm.value) params.append('search', searchTerm.value);
    params.append('page', page.value.toString());
    
    const response = await fetch(`/api/${props.endpoint}?${params}`);
    const data = await response.json();
    
    items.value = data.data;
    totalPages.value = data.pagination.totalPages;
    
    if (data.metadata?.filters) {
      availableFilters.value = data.metadata.filters;
    }
  } finally {
    loading.value = false;
  }
}

function onSearchChange() {
  page.value = 1;
  search();
}

function prevPage() {
  if (page.value > 1) {
    page.value--;
    search();
  }
}

function nextPage() {
  if (page.value < totalPages.value) {
    page.value++;
    search();
  }
}

function getLabel(filter: string): string {
  const labels: Record<string, string> = {
    firstName: 'Nombre',
    lastName: 'Apellido',
    team: 'Equipo',
    position: 'Puesto',
    name: 'Nombre',
    description: 'Descripción',
    id: 'ID'
  };
  return labels[filter] || filter;
}

// Cargar datos iniciales
search();
</script>
```

### Uso del componente:

```vue
<!-- Colaboradores -->
<SearchComponent endpoint="collaborators" entity-name="colaboradores">
  <template #default="{ item }">
    <div>{{ item.firstName }} {{ item.lastName }}</div>
  </template>
</SearchComponent>

<!-- Puestos -->
<SearchComponent endpoint="positions" entity-name="puestos">
  <template #default="{ item }">
    <div>{{ item.name }}</div>
  </template>
</SearchComponent>

<!-- Equipos -->
<SearchComponent endpoint="teams" entity-name="equipos">
  <template #default="{ item }">
    <div>{{ item.name }}</div>
  </template>
</SearchComponent>
```

---

## 📝 Composable Reutilizable (Vue 3)

```typescript
// useSearch.ts
import { ref } from 'vue';

export function useSearch<T>(endpoint: string) {
  const items = ref<T[]>([]);
  const filters = ref<string[]>([]);
  const selectedFilter = ref('');
  const searchTerm = ref('');
  const page = ref(1);
  const totalPages = ref(1);
  const totalItems = ref(0);
  const loading = ref(false);
  
  async function search() {
    loading.value = true;
    try {
      const params = new URLSearchParams();
      if (selectedFilter.value) params.append('filter', selectedFilter.value);
      if (searchTerm.value) params.append('search', searchTerm.value);
      params.append('page', page.value.toString());
      
      const response = await fetch(`/api/${endpoint}?${params}`);
      const data = await response.json();
      
      items.value = data.data;
      totalPages.value = data.pagination.totalPages;
      totalItems.value = data.pagination.totalItems;
      
      if (data.metadata?.filters) {
        filters.value = data.metadata.filters;
      }
    } finally {
      loading.value = false;
    }
  }
  
  function reset() {
    selectedFilter.value = '';
    searchTerm.value = '';
    page.value = 1;
    search();
  }
  
  return {
    items,
    filters,
    selectedFilter,
    searchTerm,
    page,
    totalPages,
    totalItems,
    loading,
    search,
    reset
  };
}

// Uso:
const { items, filters, search } = useSearch('collaborators');
const { items, filters, search } = useSearch('positions');
const { items, filters, search } = useSearch('teams');
```

---

## 🧪 Testing Rápido

```bash
# Collaborators
curl "http://localhost:8080/api/collaborators?filter=firstName&search=juan" | jq
curl "http://localhost:8080/api/collaborators" | jq '.metadata.filters'

# Positions
curl "http://localhost:8080/api/positions?filter=name&search=developer" | jq
curl "http://localhost:8080/api/positions" | jq '.metadata.filters'

# Teams
curl "http://localhost:8080/api/teams?filter=name&search=backend" | jq
curl "http://localhost:8080/api/teams" | jq '.metadata.filters'
```

---

## ✅ Checklist de Integración

- [ ] Implementar función genérica de búsqueda
- [ ] Crear componente reutilizable para los 3 endpoints
- [ ] Usar metadata.filters para generar selectores dinámicos
- [ ] Implementar paginación
- [ ] Agregar loading states
- [ ] Manejar errores
- [ ] Testing en los 3 endpoints

---

## 📚 Documentación Completa

- **[SEARCH_IMPLEMENTATION_SUMMARY.md](SEARCH_IMPLEMENTATION_SUMMARY.md)** - Resumen técnico
- **[FRONTEND_QUICK_START.md](FRONTEND_QUICK_START.md)** - Guía de integración
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html

---

**Todos los endpoints están listos y funcionando!** 🚀

