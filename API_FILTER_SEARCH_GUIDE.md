# Cambio en API de Búsqueda de Colaboradores - Filter y Search

## 📝 Resumen del Cambio

Se ha refactorizado el endpoint de búsqueda de colaboradores para usar un esquema más intuitivo con dos parámetros principales: `filter` y `search`.

## 🔄 Antes vs Ahora

### ❌ Antes (Múltiples parámetros específicos):
```bash
GET /api/collaborators?team=Backend&position=Developer&search=juan
```

### ✅ Ahora (Filter dinámico + Search):
```bash
GET /api/collaborators?filter=team&search=backend
GET /api/collaborators?filter=position&search=developer
GET /api/collaborators?filter=firstName&search=juan
```

## 🎯 Parámetros del API

| Parámetro | Tipo | Descripción | Requerido | Ejemplo |
|-----------|------|-------------|-----------|---------|
| `filter` | String | Campo sobre el que buscar | No | `firstName`, `lastName`, `team`, `position`, `id` |
| `search` | String | Valor a buscar | No | `juan`, `developer`, `backend` |
| `page` | Integer | Número de página (desde 1) | No (default: 1) | `1`, `2`, `3` |
| `size` | Integer | Elementos por página | No (default: 20) | `10`, `20`, `50` |
| `sortBy` | String | Campo para ordenar | No (default: `firstName`) | `firstName`, `lastName`, `team` |
| `sortDir` | String | Dirección de ordenamiento | No (default: `asc`) | `asc`, `desc` |

## 📚 Valores Válidos para `filter`

| Valor | Descripción | Ejemplo de uso |
|-------|-------------|----------------|
| `firstName` | Busca en el nombre del colaborador | `?filter=firstName&search=maria` |
| `lastName` | Busca en el apellido del colaborador | `?filter=lastName&search=garcia` |
| `team` | Busca en el equipo del colaborador | `?filter=team&search=backend` |
| `position` | Busca en el puesto del colaborador | `?filter=position&search=developer` |
| `id` | Busca en el ID del colaborador | `?filter=id&search=coll-0001` |
| _(omitido)_ | Sin especificar, busca en todos los campos | `?search=juan` |

## 🔍 Comportamiento de la Búsqueda

- **Case-insensitive**: `search=JUAN` es igual a `search=juan`
- **Búsqueda parcial**: `search=dev` encontrará "Developer", "DevOps", etc.
- **Búsqueda global**: Si no se especifica `filter`, busca en todos los campos

## 💻 Ejemplos de Integración Frontend

### React/TypeScript:
```typescript
// Interfaz para los parámetros de búsqueda
interface SearchParams {
  filter?: 'firstName' | 'lastName' | 'team' | 'position' | 'id';
  search?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}

// Función para buscar colaboradores
async function searchCollaborators(params: SearchParams) {
  const queryParams = new URLSearchParams();
  
  if (params.filter) queryParams.append('filter', params.filter);
  if (params.search) queryParams.append('search', params.search);
  if (params.page) queryParams.append('page', params.page.toString());
  if (params.size) queryParams.append('size', params.size.toString());
  if (params.sortBy) queryParams.append('sortBy', params.sortBy);
  if (params.sortDir) queryParams.append('sortDir', params.sortDir);
  
  const response = await fetch(`/api/collaborators?${queryParams}`);
  return response.json();
}

// Ejemplos de uso:

// Buscar por nombre
searchCollaborators({ 
  filter: 'firstName', 
  search: 'maria' 
});

// Buscar por equipo con paginación
searchCollaborators({ 
  filter: 'team', 
  search: 'backend', 
  page: 1, 
  size: 20 
});

// Búsqueda global
searchCollaborators({ 
  search: 'developer' 
});

// Ordenar por apellido descendente
searchCollaborators({ 
  sortBy: 'lastName', 
  sortDir: 'desc' 
});
```

### Angular:
```typescript
// Servicio
export class CollaboratorService {
  private apiUrl = '/api/collaborators';
  
  searchCollaborators(params: {
    filter?: string;
    search?: string;
    page?: number;
    size?: number;
    sortBy?: string;
    sortDir?: string;
  }): Observable<PageResponse<Collaborator>> {
    let httpParams = new HttpParams();
    
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        httpParams = httpParams.append(key, value.toString());
      }
    });
    
    return this.http.get<PageResponse<Collaborator>>(
      this.apiUrl, 
      { params: httpParams }
    );
  }
}

// Componente
export class CollaboratorSearchComponent {
  searchForm = this.fb.group({
    filter: ['firstName'],
    search: [''],
    page: [1],
    size: [20]
  });
  
  onSearch() {
    this.collaboratorService
      .searchCollaborators(this.searchForm.value)
      .subscribe(response => {
        this.collaborators = response.data;
        this.pagination = response.pagination;
      });
  }
}
```

### Vue.js:
```javascript
// Composable
import { ref } from 'vue';

export function useCollaboratorSearch() {
  const collaborators = ref([]);
  const pagination = ref(null);
  const loading = ref(false);
  
  async function search({ filter, search, page = 1, size = 20 }) {
    loading.value = true;
    try {
      const params = new URLSearchParams();
      if (filter) params.append('filter', filter);
      if (search) params.append('search', search);
      params.append('page', page);
      params.append('size', size);
      
      const response = await fetch(`/api/collaborators?${params}`);
      const data = await response.json();
      
      collaborators.value = data.data;
      pagination.value = data.pagination;
    } finally {
      loading.value = false;
    }
  }
  
  return { collaborators, pagination, loading, search };
}
```

## 📊 Estructura de Respuesta

La respuesta mantiene el formato simplificado:

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
      "tags": ["Java", "Spring"],
      "createdAt": "2025-11-10T10:00:00",
      "updatedAt": "2025-11-10T10:00:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalItems": 100,
    "totalPages": 5
  }
}
```

## 🎨 Ejemplo de UI - Select de Filtro

```html
<select v-model="filterType">
  <option value="firstName">Nombre</option>
  <option value="lastName">Apellido</option>
  <option value="team">Equipo</option>
  <option value="position">Posición</option>
  <option value="id">ID</option>
</select>

<input 
  type="text" 
  v-model="searchValue" 
  placeholder="Buscar..."
  @input="onSearch"
/>
```

## ⚠️ Breaking Changes

**Este cambio es incompatible con versiones anteriores del API.**

### Migración necesaria:

```diff
- GET /api/collaborators?team=Backend&position=Developer
+ GET /api/collaborators?filter=team&search=Backend
+ GET /api/collaborators?filter=position&search=Developer
```

## ✅ Ventajas del Nuevo Esquema

1. **Más flexible**: Un solo endpoint maneja todas las búsquedas
2. **Más claro**: El parámetro `filter` indica explícitamente el campo
3. **Más escalable**: Fácil agregar nuevos campos de búsqueda
4. **Búsqueda global**: Omitir `filter` busca en todos los campos
5. **Mejor UX**: Permite crear un selector de filtro en el frontend

## 🧪 Testing

### cURL Examples:
```bash
# Buscar por nombre
curl "http://localhost:8080/api/collaborators?filter=firstName&search=maria"

# Buscar por equipo
curl "http://localhost:8080/api/collaborators?filter=team&search=backend"

# Búsqueda global
curl "http://localhost:8080/api/collaborators?search=developer"

# Con paginación
curl "http://localhost:8080/api/collaborators?filter=position&search=senior&page=2&size=10"
```

## 📝 Documentación Completa

Para más detalles, consulta:
- [COLLABORATORS_SEARCH_API.md](COLLABORATORS_SEARCH_API.md) - Documentación completa del API

## 🚀 Estado

- ✅ Backend implementado y compilado
- ✅ Documentación actualizada
- ✅ Ejemplos de integración incluidos
- 🔄 Pendiente: Actualización del frontend

---

**Fecha de implementación**: 10 de Noviembre, 2025
**Versión del API**: 2.0

