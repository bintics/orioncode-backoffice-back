# API Usage Guide

## Quick Start

### 1. Start the Application

```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080/api`

### 2. Access API Documentation

Open your browser and navigate to:
```
http://localhost:8080/api/swagger-ui.html
```

## API Examples

### Position Management

#### Create a Position
```bash
curl -X POST http://localhost:8080/api/positions \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Desarrollador Full Stack",
    "description": "Desarrollador con experiencia en frontend y backend"
  }'
```

#### Get All Positions
```bash
curl http://localhost:8080/api/positions
```

#### Get Position by ID
```bash
curl http://localhost:8080/api/positions/1
```

#### Update Position
```bash
curl -X PUT http://localhost:8080/api/positions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Senior Full Stack Developer",
    "description": "Senior developer with full stack expertise"
  }'
```

#### Delete Position
```bash
curl -X DELETE http://localhost:8080/api/positions/1
```

### Employee Management

#### Create an Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "Juan",
    "lastName": "Pérez García",
    "positionId": 1,
    "team": "Desarrollo Backend",
    "tags": ["Java", "Spring Boot", "MySQL", "Docker"]
  }'
```

#### Get All Employees
```bash
curl http://localhost:8080/api/employees
```

#### Get Employee by ID
```bash
curl http://localhost:8080/api/employees/1
```

#### Get Employees by Team
```bash
curl http://localhost:8080/api/employees/team/Desarrollo%20Backend
```

#### Get Employees by Position
```bash
curl http://localhost:8080/api/employees/position/1
```

#### Update Employee
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "Juan Carlos",
    "lastName": "Pérez García",
    "positionId": 1,
    "team": "Desarrollo Full Stack",
    "tags": ["Java", "Spring Boot", "MySQL", "Docker", "React"]
  }'
```

#### Delete Employee
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

## Example Workflow

Here's a complete workflow to create positions and employees:

```bash
# 1. Create positions
curl -X POST http://localhost:8080/api/positions \
  -H "Content-Type: application/json" \
  -d '{"name": "Desarrollador Backend", "description": "Backend developer"}'

curl -X POST http://localhost:8080/api/positions \
  -H "Content-Type: application/json" \
  -d '{"name": "Desarrollador Frontend", "description": "Frontend developer"}'

curl -X POST http://localhost:8080/api/positions \
  -H "Content-Type: application/json" \
  -d '{"name": "DevOps Engineer", "description": "Infrastructure and deployment"}'

# 2. Create employees for Backend team
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "BACK001",
    "firstName": "María",
    "lastName": "González",
    "positionId": 1,
    "team": "Backend Team",
    "tags": ["Java", "Spring", "PostgreSQL"]
  }'

curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "BACK002",
    "firstName": "Carlos",
    "lastName": "Rodríguez",
    "positionId": 1,
    "team": "Backend Team",
    "tags": ["Java", "Spring Boot", "MySQL", "Redis"]
  }'

# 3. Create employees for Frontend team
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "FRONT001",
    "firstName": "Ana",
    "lastName": "Martínez",
    "positionId": 2,
    "team": "Frontend Team",
    "tags": ["React", "TypeScript", "CSS"]
  }'

# 4. Query employees by team
curl http://localhost:8080/api/employees/team/Backend%20Team

# 5. Query employees by position
curl http://localhost:8080/api/employees/position/1
```

## Response Format

### Success Response
```json
{
  "id": 1,
  "employeeCode": "EMP001",
  "firstName": "Juan",
  "lastName": "Pérez García",
  "position": {
    "id": 1,
    "name": "Desarrollador Full Stack",
    "description": "Desarrollador con experiencia en frontend y backend",
    "createdAt": "2024-11-07T01:30:00",
    "updatedAt": "2024-11-07T01:30:00"
  },
  "team": "Desarrollo Backend",
  "tags": ["Java", "Spring Boot", "MySQL", "Docker"],
  "createdAt": "2024-11-07T01:35:00",
  "updatedAt": "2024-11-07T01:35:00"
}
```

### Error Response
```json
{
  "timestamp": "2024-11-07T01:40:00",
  "status": 404,
  "error": "Not Found",
  "message": "Colaborador no encontrado con ID: 999",
  "path": "/api/employees/999"
}
```

### Validation Error Response
```json
{
  "timestamp": "2024-11-07T01:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "firstName": "El nombre es requerido",
    "employeeCode": "El código de colaborador es requerido"
  },
  "path": "/api/employees"
}
```
