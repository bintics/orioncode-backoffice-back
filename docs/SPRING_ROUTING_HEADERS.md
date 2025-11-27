# Spring Routing Automático con Headers

## Explicación

En el `PositionController`, se utilizan **dos métodos distintos** con `@GetMapping` que responden al **mismo endpoint** `/positions`, pero Spring los diferencia automáticamente basándose en la **presencia del header `X-dropdown`**.

## Cómo Funciona

### 1. Sin header X-dropdown → `searchPositions()`
```java
@GetMapping
public ResponseEntity<PageResponse<PositionResponseDTO>> searchPositions(...)
```
- Spring ejecuta este método cuando **NO viene** el header `X-dropdown`
- Retorna búsqueda completa con paginación

### 2. Con header X-dropdown → `getPositionsForDropdown()`
```java
@GetMapping(headers = "X-dropdown")
public ResponseEntity<List<PositionResponseDTO>> getPositionsForDropdown(...)
```
- Spring ejecuta este método cuando **SÍ viene** el header `X-dropdown`
- Retorna máximo 20 registros sin paginación

## Ventajas de esta Implementación

1. ✅ **Sin lógica condicional**: No hay `if` statements en el código
2. ✅ **Routing automático**: Spring decide qué método ejecutar
3. ✅ **Métodos independientes**: Cada método tiene su propia responsabilidad
4. ✅ **Tipos de retorno específicos**: Cada método retorna el tipo correcto (no `ResponseEntity<?>`)
5. ✅ **Más limpio**: El código es más fácil de leer y mantener

## Ejemplos de Uso

### Desde el Frontend

#### Búsqueda con paginación
```javascript
fetch('/positions?page=1&size=20&filter=name&search=backend')
  .then(response => response.json())
  .then(data => {
    // data = {data: [...], pagination: {...}, metadata: {...}}
  });
```

#### Dropdown
```javascript
fetch('/positions?search=backend', {
  headers: { 'X-dropdown': 'true' }
})
  .then(response => response.json())
  .then(data => {
    // data = [{id: "pos-001", name: "...", ...}, ...]
  });
```

### Desde curl

#### Búsqueda con paginación
```bash
curl http://localhost:8080/positions?page=1&size=20
```

#### Dropdown
```bash
curl -H "X-dropdown: true" http://localhost:8080/positions?search=backend
```

## Nota Importante

El valor del header `X-dropdown` **no importa**. Spring solo verifica si el header **existe**. Estos son válidos:
- `X-dropdown: true`
- `X-dropdown: 1`
- `X-dropdown: yes`
- `X-dropdown: cualquier-valor`

Todos activan el método `getPositionsForDropdown()`.

