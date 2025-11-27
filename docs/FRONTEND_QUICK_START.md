# 🎨 Guía Rápida para Frontend - API Filter & Search

## 📍 Endpoint

```
GET /api/collaborators
```

## 🎯 Parámetros que Recibirán del Usuario

| Campo UI | Parámetro API | Valores Posibles |
|----------|---------------|------------------|
| Selector de Campo | `filter` | `firstName`, `lastName`, `team`, `position`, `id` |
| Input de Búsqueda | `search` | Cualquier texto |
| Número de Página | `page` | 1, 2, 3... |
| Elementos por Página | `size` | 10, 20, 50... |

## 💡 Implementación Sugerida

### Componente de Búsqueda:

```html
<!-- HTML -->
<div class="search-container">
  <select id="filter" v-model="filter">
    <option value="">Todos los campos</option>
    <option value="firstName">Nombre</option>
    <option value="lastName">Apellido</option>
    <option value="team">Equipo</option>
    <option value="position">Posición</option>
    <option value="id">ID</option>
  </select>
  
  <input 
    type="text" 
    v-model="search" 
    placeholder="Buscar..."
    @input="onSearchChange"
  />
  
  <button @click="doSearch">Buscar</button>
</div>
```

### JavaScript/TypeScript:

```javascript
// Estado
const filter = ref('');
const search = ref('');
const page = ref(1);
const size = ref(20);

// Función de búsqueda
async function doSearch() {
  const params = new URLSearchParams();
  
  // Solo agregar filter si no está vacío
  if (filter.value) {
    params.append('filter', filter.value);
  }
  
  // Solo agregar search si no está vacío
  if (search.value) {
    params.append('search', search.value);
  }
  
  params.append('page', page.value);
  params.append('size', size.value);
  
  const url = `/api/collaborators?${params.toString()}`;
  const response = await fetch(url);
  const data = await response.json();
  
  // data.data contiene los colaboradores
  // data.pagination contiene la info de paginación
  collaborators.value = data.data;
  pagination.value = data.pagination;
}
```

## 📊 Ejemplos de Casos de Uso

### Caso 1: Usuario selecciona "Nombre" y escribe "maria"
```javascript
// Estado:
filter = "firstName"
search = "maria"

// URL generada:
GET /api/collaborators?filter=firstName&search=maria&page=1&size=20
```

### Caso 2: Usuario NO selecciona campo (búsqueda global) y escribe "developer"
```javascript
// Estado:
filter = ""
search = "developer"

// URL generada:
GET /api/collaborators?search=developer&page=1&size=20

// Busca en: firstName, lastName, team, position, id
```

### Caso 3: Usuario selecciona "Equipo" y escribe "backend"
```javascript
// Estado:
filter = "team"
search = "backend"

// URL generada:
GET /api/collaborators?filter=team&search=backend&page=1&size=20
```

### Caso 4: Paginación (página 2)
```javascript
// Estado:
filter = "position"
search = "developer"
page = 2

// URL generada:
GET /api/collaborators?filter=position&search=developer&page=2&size=20
```

## 🎨 UI Sugerido

```
┌─────────────────────────────────────────────────┐
│  Buscar Colaboradores                           │
├─────────────────────────────────────────────────┤
│                                                 │
│  Campo:  [▼ Nombre     ]  Buscar: [maria___]   │
│                            [Buscar] [Limpiar]   │
│                                                 │
├─────────────────────────────────────────────────┤
│  Resultados: 15 colaboradores encontrados      │
│                                                 │
│  ┌─────────────────────────────────────────┐  │
│  │ María García                            │  │
│  │ Posición: Software Developer            │  │
│  │ Equipo: Desarrollo Backend              │  │
│  └─────────────────────────────────────────┘  │
│                                                 │
│  ... más resultados ...                        │
│                                                 │
│  [◀] Página 1 de 3 [▶]                         │
└─────────────────────────────────────────────────┘
```

## 🔄 Flujo de Búsqueda

```
Usuario escribe en input
       ↓
Debounce 300ms (opcional)
       ↓
Construir URLSearchParams
       ↓
Llamar API
       ↓
Actualizar lista + paginación
       ↓
Mostrar resultados
```

## 📦 Estructura de Respuesta que Recibirán

```typescript
interface PageResponse<T> {
  data: T[];
  pagination: {
    page: number;        // Página actual (1, 2, 3...)
    pageSize: number;    // Elementos por página
    totalItems: number;  // Total de elementos
    totalPages: number;  // Total de páginas
  };
  metadata: {
    filters: string[];   // Campos disponibles para filtrar
  };
}

interface Collaborator {
  id: string;
  firstName: string;
  lastName: string;
  position: string;
  team: {
    id: string | null;
    name: string;
  } | null;
  tags: string[];
  createdAt: string;
  updatedAt: string;
}
```

## 🎯 Componente Completo de Ejemplo (Vue 3)

```vue
<template>
  <div class="collaborator-search">
        <option 
          v-for="filter in availableFilters" 
          :key="filter" 
          :value="filter"
        >
          {{ getFilterLabel(filter) }}
        </option>
        <option value="team">Equipo</option>
        <option value="position">Posición</option>
        <option value="id">ID</option>
      </select>
      
      <input
        v-model="searchParams.search"
        type="text"
        placeholder="Buscar..."
        @input="onSearchInput"
        class="search-input"
      />
      
      <button @click="performSearch" class="search-button">
        Buscar
      </button>
    </div>
    
    <div v-if="loading" class="loading">
      Cargando...
    </div>
    
    <div v-else-if="collaborators.length > 0">
      <div class="results-info">
        {{ pagination.totalItems }} colaboradores encontrados
      </div>
      
      <div
        v-for="collab in collaborators"
        :key="collab.id"
        class="collaborator-card"
      >
        <h3>{{ collab.firstName }} {{ collab.lastName }}</h3>
        <p>Posición: {{ collab.position }}</p>
        <p v-if="collab.team">Equipo: {{ collab.team.name }}</p>
        <div class="tags">
          <span v-for="tag in collab.tags" :key="tag" class="tag">
            {{ tag }}
          </span>
        </div>
      </div>
      
      <div class="pagination">
        <button
          @click="changePage(pagination.page - 1)"
          :disabled="pagination.page === 1"
        >
          ◀ Anterior
        </button>
        
        <span>
          Página {{ pagination.page }} de {{ pagination.totalPages }}
        </span>
        
        <button
          @click="changePage(pagination.page + 1)"
          :disabled="pagination.page === pagination.totalPages"
        >
          Siguiente ▶
        </button>
      </div>
    </div>
    
    <div v-else class="no-results">
      No se encontraron resultados
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';

const collaborators = ref([]);
const pagination = ref({
  page: 1,
  pageSize: 20,
const availableFilters = ref([]);
  totalItems: 0,
  totalPages: 0
});
const loading = ref(false);

const searchParams = reactive({
  filter: '',
  search: '',
  page: 1,
  size: 20
});

async function performSearch() {
  loading.value = true;
  
  try {
    const params = new URLSearchParams();
    
    if (searchParams.filter) {
      params.append('filter', searchParams.filter);
    }
    
    if (searchParams.search) {
      params.append('search', searchParams.search);
    }
    
    params.append('page', searchParams.page);
    params.append('size', searchParams.size);
    
    const response = await fetch(`/api/collaborators?${params}`);
    
    // Actualizar filtros disponibles desde metadata (solo la primera vez)
    if (data.metadata && data.metadata.filters) {
      availableFilters.value = data.metadata.filters;
    }
    const data = await response.json();
    
    collaborators.value = data.data;
    pagination.value = data.pagination;
  } catch (error) {
    console.error('Error al buscar:', error);
  } finally {
    loading.value = false;
  }
}

function changePage(newPage) {
  searchParams.page = newPage;
  performSearch();

// Helper para convertir nombres de filtros a labels legibles
function getFilterLabel(filter) {
  const labels = {
    firstName: 'Nombre',
    lastName: 'Apellido',
    team: 'Equipo',
    position: 'Posición',
    id: 'ID'
  };
  return labels[filter] || filter;
}
}

// Debounce opcional para búsqueda mientras escribe
let searchTimeout;
function onSearchInput() {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    searchParams.page = 1; // Reset a página 1
    performSearch();
  }, 300);
}
</script>
```

## 🐛 Debugging

### Ver la URL que se está llamando:
```javascript
console.log(`Llamando: /api/collaborators?${params.toString()}`);
```

### Verificar respuesta:
```javascript
const data = await response.json();
console.log('Data:', data.data);
console.log('Pagination:', data.pagination);
```

## ✅ Checklist de Integración

- [ ] Crear selector de campo (`filter`)
- [ ] Crear input de búsqueda (`search`)
- [ ] Implementar llamada a API
- [ ] Parsear respuesta (`data.data` y `data.pagination`)
- [ ] Mostrar resultados en lista
- [ ] Implementar paginación
- [ ] Agregar loading state
- [ ] Manejar errores
- [ ] Agregar debounce (opcional)
- [ ] Testing

## 📞 URLs Útiles

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Base**: http://localhost:8080/api/collaborators
- **Ejemplo**: http://localhost:8080/api/collaborators?filter=firstName&search=maria

---

**¿Dudas?** Consulta `API_FILTER_SEARCH_GUIDE.md` para más ejemplos en React, Angular y Vue.

