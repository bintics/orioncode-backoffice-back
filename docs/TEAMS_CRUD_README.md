# CRUD de Teams - Documentación

## Resumen de Cambios

Se ha implementado el CRUD completo para la entidad **Teams** y se han actualizado todas las entidades (Employee, Position, Team) para usar **String (UUID)** como tipo de ID en lugar de Long.

## Estructura Creada para Teams

### 1. Entidad (Entity)
- **Ubicación**: `src/main/java/com/orioncode/backoffice/team/entity/Team.java`
- **Campos**:
  - `id` (String - UUID)
  - `name` (String - único, requerido)
  - `description` (String - opcional)
  - `createdAt` (LocalDateTime)
  - `updatedAt` (LocalDateTime)

### 2. DTOs
- **TeamRequestDTO**: `src/main/java/com/orioncode/backoffice/team/dto/TeamRequestDTO.java`
  - Campos: name, description
  
- **TeamResponseDTO**: `src/main/java/com/orioncode/backoffice/team/dto/TeamResponseDTO.java`
  - Campos: id, name, description, createdAt, updatedAt

### 3. Repository
- **Ubicación**: `src/main/java/com/orioncode/backoffice/team/repository/TeamRepository.java`
- **Métodos**:
  - `findByName(String name)`
  - `existsByName(String name)`

### 4. Service
- **Ubicación**: `src/main/java/com/orioncode/backoffice/team/service/TeamService.java`
- **Métodos**:
  - `getAllTeams()` - Obtener todos los equipos
  - `getTeamById(String id)` - Obtener equipo por ID
  - `getTeamByName(String name)` - Obtener equipo por nombre
  - `createTeam(TeamRequestDTO)` - Crear nuevo equipo (genera UUID automáticamente)
  - `updateTeam(String id, TeamRequestDTO)` - Actualizar equipo
  - `deleteTeam(String id)` - Eliminar equipo

### 5. Controller
- **Ubicación**: `src/main/java/com/orioncode/backoffice/team/controller/TeamController.java`
- **Endpoints**:
  - `GET /teams` - Listar todos los equipos
  - `GET /teams/{id}` - Obtener equipo por ID
  - `GET /teams/name/{name}` - Obtener equipo por nombre
  - `POST /teams` - Crear nuevo equipo
  - `PUT /teams/{id}` - Actualizar equipo
  - `DELETE /teams/{id}` - Eliminar equipo

### 6. Tests
- **Ubicación**: `src/test/java/com/orioncode/backoffice/team/TeamServiceTest.java`
- Tests implementados para todas las operaciones CRUD

## Cambios en IDs a String (UUID)

### Employee (Collaborators)
- ✅ ID cambiado de Long a String
- ✅ Se genera UUID automáticamente si no se proporciona
- ✅ Repository actualizado: `JpaRepository<Employee, String>`
- ✅ Métodos `findByPositionId` ahora recibe String

### Position
- ✅ ID cambiado de Long a String
- ✅ Se genera UUID automáticamente al crear
- ✅ Repository actualizado: `JpaRepository<Position, String>`

### Team
- ✅ ID tipo String desde el inicio
- ✅ Se genera UUID automáticamente al crear
- ✅ Repository: `JpaRepository<Team, String>`

## Ejemplos de Uso

### Crear un Team
```bash
curl -X POST http://localhost:8080/teams \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Development Team",
    "description": "Team de desarrollo backend"
  }'
```

### Obtener todos los Teams
```bash
curl http://localhost:8080/teams
```

### Obtener Team por ID
```bash
curl http://localhost:8080/teams/{uuid}
```

### Obtener Team por nombre
```bash
curl http://localhost:8080/teams/name/Development%20Team
```

### Actualizar un Team
```bash
curl -X PUT http://localhost:8080/teams/{uuid} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Development Team",
    "description": "Equipo de desarrollo backend actualizado"
  }'
```

### Eliminar un Team
```bash
curl -X DELETE http://localhost:8080/teams/{uuid}
```

## Notas Importantes

1. **UUIDs**: Todos los IDs ahora son UUIDs generados automáticamente en formato String
2. **Validaciones**: 
   - Los nombres de teams deben ser únicos
   - El servicio valida que no existan duplicados antes de crear/actualizar
3. **Swagger**: Todos los endpoints están documentados con OpenAPI/Swagger
4. **Transacciones**: Todas las operaciones de escritura están marcadas como `@Transactional`

## Próximos Pasos Sugeridos

1. Considerar cambiar el campo `team` en Employee de String a una relación `@ManyToOne` con la entidad Team
2. Agregar validaciones adicionales con `@Valid` en los DTOs
3. Implementar paginación para los métodos de listado
4. Agregar filtros adicionales según necesidades del frontend

## Compatibilidad

- ✅ Spring Boot 3.x
- ✅ Java 17+
- ✅ JPA/Hibernate
- ✅ H2/MySQL/PostgreSQL compatible

