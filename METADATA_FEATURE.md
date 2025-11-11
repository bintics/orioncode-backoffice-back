# ✅ Actualización: Metadata con Filtros Disponibles

## 📝 Resumen del Cambio

Se agregó un nuevo nodo `metadata` a la respuesta del endpoint de búsqueda de colaboradores que contiene la lista de campos disponibles para filtrar. Esto permite que el frontend sea completamente dinámico y no necesite hardcodear los filtros.

---

## 🎯 Cambio Implementado

### Nueva Estructura de Respuesta:

```json
{
  "data": [...],
  "pagination": {...},
  "metadata": {
    "filters": [
      "firstName",
      "lastName",
      "team",
      "position",
      "id"
    ]
  }
}
```

---

## 📊 Estructura Completa de Respuesta

```json
{
  "data": [
    {
      "id": "coll-0046",
      "firstName": "Ana",
      "lastName": "Martínez",
      "position": "Software Developer",
      "team": {
        "id": "team-002",
        "name": "Equipo Alfa"
      },
      "tags": ["React", "TypeScript", "UI/UX"],
      "createdAt": "2023-11-10T09:15:00Z",
      "updatedAt": "2024-05-20T14:30:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 10,
    "totalItems": 1,
    "totalPages": 1
  },
  "metadata": {
    "filters": [
      "firstName",
      "lastName",
      "team",
      "position",
      "id"
    ]
  }
}
```

---

## 💡 Ventajas del Metadata

### ✅ Frontend Dinámico
El frontend puede generar automáticamente las opciones del selector de filtros:

```javascript
// Antes (hardcodeado):
<select>
  <option value="firstName">Nombre</option>
  <option value="lastName">Apellido</option>
  <option value="team">Equipo</option>
  ...
</select>

// Ahora (dinámico):
<select>
  <option v-for="filter in metadata.filters" :value="filter">
    {{ filter }}
  </option>
</select>
```

### ✅ Fácil de Mantener
- Si se agregan o quitan filtros en el backend, el frontend se adapta automáticamente
- No requiere cambios en el código del frontend
- Single source of truth

### ✅ Descubrimiento del API
El frontend conoce inmediatamente qué campos están disponibles sin necesidad de documentación adicional

---

## 🛠️ Implementación Backend

### 1. Nueva Clase `SearchMetadata`:
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchMetadata {
    private List<String> filters;
}
```

### 2. Actualización de `PageResponse`:
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> data;
    private PaginationMetadata pagination;
    private SearchMetadata metadata;  // ← NUEVO
}
```

### 3. Servicio actualizado:
```java
SearchMetadata metadata = new SearchMetadata(
    List.of("firstName", "lastName", "team", "position", "id")
);

return new PageResponse<>(data, pagination, metadata);
```

---

## 💻 Uso en Frontend

### TypeScript Interface:
```typescript
interface PageResponse<T> {
  data: T[];
  pagination: {
    page: number;
    pageSize: number;
    totalItems: number;
    totalPages: number;
  };
  metadata: {
    filters: string[];  // ← NUEVO
  };
}
```

### Ejemplo Vue 3:
```vue
<template>
  <select v-model="selectedFilter">
    <option value="">Todos los campos</option>
    <option 
      v-for="filter in availableFilters" 
      :key="filter" 
      :value="filter"
    >
      {{ getFilterLabel(filter) }}
    </option>
  </select>
</template>

<script setup>
const availableFilters = ref([]);

async function fetchData() {
  const response = await fetch('/api/collaborators');
  const data = await response.json();
  
  // Guardar filtros disponibles
  availableFilters.value = data.metadata.filters;
}
</script>
```

### Ejemplo React:
```typescript
function CollaboratorSearch() {
  const [availableFilters, setAvailableFilters] = useState<string[]>([]);
  
  useEffect(() => {
    fetch('/api/collaborators')
      .then(res => res.json())
      .then(data => {
        setAvailableFilters(data.metadata.filters);
      });
  }, []);
  
  return (
    <select>
      <option value="">Todos los campos</option>
      {availableFilters.map(filter => (
        <option key={filter} value={filter}>
          {getFilterLabel(filter)}
        </option>
      ))}
    </select>
  );
}
```

### Ejemplo Angular:
```typescript
export class SearchComponent implements OnInit {
  availableFilters: string[] = [];
  
  ngOnInit() {
    this.http.get<PageResponse>('/api/collaborators')
      .subscribe(response => {
        this.availableFilters = response.metadata.filters;
      });
  }
}
```

---

## 🎨 Helper para Labels Legibles

```javascript
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
```

O con i18n:
```javascript
function getFilterLabel(filter) {
  return i18n.t(`filters.${filter}`);
}
```

---

## 🔄 Flujo Recomendado

```
1. Usuario carga la página
       ↓
2. Primera llamada al API
       ↓
3. Guardar metadata.filters en estado
       ↓
4. Generar opciones del selector dinámicamente
       ↓
5. Usuario selecciona filtro y busca
```

---

## 📋 Checklist de Actualización Frontend

- [ ] Actualizar interface/type de PageResponse
- [ ] Agregar estado para `availableFilters`
- [ ] Capturar `metadata.filters` de la respuesta
- [ ] Generar opciones del selector dinámicamente
- [ ] Agregar helper para labels legibles (opcional)
- [ ] Manejar caso cuando metadata es null (compatibilidad)
- [ ] Testing

---

## ⚠️ Compatibilidad

El `PageResponse` incluye un constructor sin `metadata` para mantener compatibilidad:

```java
public PageResponse(List<T> data, PaginationMetadata pagination) {
    this.data = data;
    this.pagination = pagination;
    this.metadata = null;  // Puede ser null
}
```

**Recomendación**: Siempre verificar si metadata existe antes de usarlo:

```javascript
if (data.metadata && data.metadata.filters) {
  availableFilters.value = data.metadata.filters;
}
```

---

## 🧪 Testing

### Verificar respuesta:
```bash
curl "http://localhost:8080/api/collaborators" | jq '.metadata'
```

**Salida esperada:**
```json
{
  "filters": [
    "firstName",
    "lastName",
    "team",
    "position",
    "id"
  ]
}
```

---

## 📝 Archivos Modificados

### Backend:
1. ✅ `SearchMetadata.java` - Nueva clase para metadata
2. ✅ `PageResponse.java` - Agregado campo metadata
3. ✅ `CollaboratorService.java` - Incluye metadata en respuesta

### Documentación:
4. ✅ `COLLABORATORS_SEARCH_API.md` - Actualizada con metadata
5. ✅ `FRONTEND_QUICK_START.md` - Ejemplos con metadata
6. ✅ `METADATA_FEATURE.md` - Este documento

---

## ✅ Verificaciones

- ✅ Código compilado sin errores
- ✅ Metadata incluido en respuesta
- ✅ Lista de filtros correcta
- ✅ Documentación actualizada
- ✅ Ejemplos de frontend incluidos

---

## 🎉 Resultado

El endpoint ahora:
- ✅ Incluye metadata con filtros disponibles
- ✅ Permite frontend 100% dinámico
- ✅ Facilita el mantenimiento
- ✅ Mejora la experiencia del desarrollador
- ✅ Mantiene compatibilidad hacia atrás

---

**Fecha**: 10 de Noviembre, 2025  
**Estado**: ✅ COMPLETADO  
**Breaking Change**: NO (metadata es opcional)

