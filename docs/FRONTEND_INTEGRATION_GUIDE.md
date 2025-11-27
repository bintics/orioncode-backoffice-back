# Guía de Integración Frontend - Teams API

## 📋 Cambios Importantes para el Frontend

### IDs ahora son UUIDs (String)

**Antes:**
```typescript
interface Employee {
  id: number;
  positionId: number;
}
```

**Ahora:**
```typescript
interface Employee {
  id: string;  // UUID
  positionId: string;  // UUID
}
```

## 🆕 Nueva Entidad: Team

### TypeScript Interfaces

```typescript
// Team Request (para crear/actualizar)
interface TeamRequest {
  name: string;
  description?: string;
}

// Team Response (del servidor)
interface TeamResponse {
  id: string;  // UUID
  name: string;
  description: string | null;
  createdAt: string;  // ISO 8601
  updatedAt: string;  // ISO 8601
}

// Employee actualizado
interface Employee {
  id: string;  // UUID
  firstName: string;
  lastName: string;
  position: Position;
  team: string;  // Por ahora es String, puede ser TeamResponse en el futuro
  tags: string[];
  createdAt: string;
  updatedAt: string;
}

// Position actualizado
interface Position {
  id: string;  // UUID
  name: string;
  description: string | null;
  createdAt: string;
  updatedAt: string;
}
```

## 🔌 API Endpoints

### Teams

```typescript
// GET /teams - Listar todos los equipos
const getTeams = async (): Promise<TeamResponse[]> => {
  const response = await fetch('http://localhost:8080/teams');
  return response.json();
};

// GET /teams/{id} - Obtener equipo por ID
const getTeamById = async (id: string): Promise<TeamResponse> => {
  const response = await fetch(`http://localhost:8080/teams/${id}`);
  return response.json();
};

// GET /teams/name/{name} - Obtener equipo por nombre
const getTeamByName = async (name: string): Promise<TeamResponse> => {
  const response = await fetch(`http://localhost:8080/teams/name/${encodeURIComponent(name)}`);
  return response.json();
};

// POST /teams - Crear equipo
const createTeam = async (team: TeamRequest): Promise<TeamResponse> => {
  const response = await fetch('http://localhost:8080/teams', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(team)
  });
  return response.json();
};

// PUT /teams/{id} - Actualizar equipo
const updateTeam = async (id: string, team: TeamRequest): Promise<TeamResponse> => {
  const response = await fetch(`http://localhost:8080/teams/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(team)
  });
  return response.json();
};

// DELETE /teams/{id} - Eliminar equipo
const deleteTeam = async (id: string): Promise<void> => {
  await fetch(`http://localhost:8080/teams/${id}`, {
    method: 'DELETE'
  });
};
```

### Employees (Actualizado)

```typescript
// POST /collaborators - Crear colaborador
const createEmployee = async (employee: {
  id?: string;  // Opcional, se genera automáticamente si no se envía
  firstName: string;
  lastName: string;
  positionId: string;  // UUID
  team?: string;
  tags?: string[];
}): Promise<Employee> => {
  const response = await fetch('http://localhost:8080/collaborators', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(employee)
  });
  return response.json();
};

// GET /collaborators/position/{positionId} - Por UUID de posición
const getEmployeesByPosition = async (positionId: string): Promise<Employee[]> => {
  const response = await fetch(`http://localhost:8080/collaborators/position/${positionId}`);
  return response.json();
};
```

### Positions (Actualizado)

```typescript
// POST /positions - Crear posición
const createPosition = async (position: {
  name: string;
  description?: string;
}): Promise<Position> => {
  const response = await fetch('http://localhost:8080/positions', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(position)
  });
  return response.json();
};
```

## 🎨 Ejemplo de Uso en React

### Hook personalizado para Teams

```typescript
import { useState, useEffect } from 'react';

interface UseTeamsResult {
  teams: TeamResponse[];
  loading: boolean;
  error: Error | null;
  refetch: () => void;
}

export const useTeams = (): UseTeamsResult => {
  const [teams, setTeams] = useState<TeamResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchTeams = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/teams');
      const data = await response.json();
      setTeams(data);
      setError(null);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTeams();
  }, []);

  return { teams, loading, error, refetch: fetchTeams };
};
```

### Componente de Formulario

```typescript
import React, { useState } from 'react';

const TeamForm: React.FC = () => {
  const [formData, setFormData] = useState<TeamRequest>({
    name: '',
    description: ''
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const response = await fetch('http://localhost:8080/teams', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });
      
      if (response.ok) {
        const team = await response.json();
        console.log('Team creado:', team);
        // Resetear formulario o redirigir
      }
    } catch (error) {
      console.error('Error al crear team:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        value={formData.name}
        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
        placeholder="Nombre del equipo"
        required
      />
      <textarea
        value={formData.description}
        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
        placeholder="Descripción"
      />
      <button type="submit">Crear Equipo</button>
    </form>
  );
};
```

## ⚠️ Consideraciones Importantes

1. **UUIDs**: Todos los IDs ahora son strings en formato UUID (ej: `"550e8400-e29b-41d4-a716-446655440000"`)
2. **Validación**: Asegúrate de validar que los IDs sean UUIDs válidos antes de enviarlos
3. **LocalStorage**: Si guardas IDs en localStorage, actualiza el tipo a string
4. **URLs**: Al construir URLs con IDs, no necesitas conversión a número

## 🔄 Migración de Código Existente

### Antes
```typescript
const employeeId = parseInt(params.id);  // ❌ Ya no es necesario
fetch(`/collaborators/${employeeId}`);
```

### Ahora
```typescript
const employeeId = params.id;  // ✅ Ya es string (UUID)
fetch(`/collaborators/${employeeId}`);
```

## 📝 Validación de UUIDs (Opcional)

```typescript
const isValidUUID = (uuid: string): boolean => {
  const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
  return uuidRegex.test(uuid);
};

// Uso
if (!isValidUUID(teamId)) {
  console.error('ID inválido');
}
```

## 🚀 Testing

```typescript
// Mock de datos para testing
const mockTeam: TeamResponse = {
  id: '550e8400-e29b-41d4-a716-446655440000',
  name: 'Backend Team',
  description: 'Equipo de desarrollo backend',
  createdAt: '2025-11-10T12:00:00Z',
  updatedAt: '2025-11-10T12:00:00Z'
};
```

## 📚 Recursos Adicionales

- [RFC 4122 - UUID Specification](https://tools.ietf.org/html/rfc4122)
- Documentación Swagger: `http://localhost:8080/swagger-ui.html`

