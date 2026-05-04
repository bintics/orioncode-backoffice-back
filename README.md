# OrionCode Backoffice Backend

API REST desarrollada con Java, Spring Boot y MySQL para la gestión de puestos y colaboradores, utilizando arquitectura de vertical slicing.

## 📋 Características

- **Gestión de Puestos**: CRUD completo para administración de puestos de trabajo
- **Gestión de Colaboradores**: CRUD completo con información de colaboradores, incluyendo:
  - Código de colaborador
  - Nombre y apellidos
  - Puesto asignado
  - Equipo al que pertenece
  - Tags para clasificación
- **Arquitectura de Vertical Slicing**: Cada módulo es independiente y puede ser promovido fácilmente a microservicio
- **Documentación API**: Swagger/OpenAPI integrado
- **Validaciones**: Validación de datos de entrada
- **Manejo de Errores**: Sistema global de manejo de excepciones

## 🏗️ Arquitectura

El proyecto utiliza **vertical slicing**, donde cada módulo contiene todas sus capas:

```
position/
├── entity/         # Entidades JPA
├── dto/            # DTOs de request/response
├── repository/     # Repositorios Spring Data
├── service/        # Lógica de negocio
└── controller/     # Controladores REST

employee/
├── entity/
├── dto/
├── repository/
├── service/
└── controller/
```

Esta arquitectura facilita la promoción de cada módulo a microservicio independiente.

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **MySQL** (producción)
- **H2 Database** (desarrollo y tests)
- **Maven**
- **Lombok**
- **SpringDoc OpenAPI (Swagger)**

## 🚀 Inicio Rápido

### Opción 1: Modo Desarrollo (Recomendado - No requiere MySQL)

```bash
# Usando el script
./run-dev.sh

# O directamente con Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Esto iniciará la aplicación con H2 en memoria y cargará datos de prueba automáticamente.

**URLs disponibles:**
- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- H2 Console: http://localhost:8080/api/h2-console

### Opción 2: Modo Producción (Requiere MySQL)

1. Asegúrate de tener MySQL corriendo
2. Ejecuta:
```bash
mvn spring-boot:run
```

Para más detalles, consulta [COMO_EJECUTAR.md](docs/COMO_EJECUTAR.md)

## 🤖 MCP (Model Context Protocol)

La integración MCP ahora usa `spring-ai-starter-mcp-server-webmvc` (MCP Server Boot Starter), por lo que el protocolo lo gestiona Spring AI en lugar de un controlador JSON-RPC manual.

- Endpoint MCP (SSE): `GET /api/mcp`
- Tools disponibles: `search_collaborators`, `search_projects`, `search_teams`
- Clases tool:
  - `src/main/java/com/orioncode/mcp/tools/tools/CollaboratorMcpTool.java`
  - `src/main/java/com/orioncode/mcp/tools/tools/ProjectMcpTool.java`
  - `src/main/java/com/orioncode/mcp/tools/tools/TeamMcpTool.java`

En Cloud Run, reemplaza `http://localhost:8090` por la URL pública de tu servicio.

## 📚 Documentación Adicional

- [Guía de Ejecución Completa](docs/COMO_EJECUTAR.md)
- [API de Búsqueda de Colaboradores](docs/COLLABORATORS_SEARCH_API.md)
- [Guía de Integración Frontend](docs/FRONTEND_INTEGRATION_GUIDE.md)
- [API de Teams](docs/TEAMS_CRUD_README.md)

## 📦 Requisitos Previos

- JDK 17 o superior
- Maven 3.6+
- MySQL 8.0+

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/bintics/orioncode-backoffice-back.git
cd orioncode-backoffice-back
```

### 2. Configurar la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE orioncode_backoffice;
```

### 3. Configurar application.properties

Editar `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/orioncode_backoffice?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 4. Compilar el proyecto

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api`

## 📚 Documentación API

Una vez la aplicación esté ejecutándose, accede a la documentación Swagger en:

```
http://localhost:8080/api/swagger-ui.html
```

## 🔌 Endpoints Principales

### Puestos (Positions)

- `GET /api/positions` - Obtener todos los puestos
- `GET /api/positions/{id}` - Obtener puesto por ID
- `POST /api/positions` - Crear nuevo puesto
- `PUT /api/positions/{id}` - Actualizar puesto
- `DELETE /api/positions/{id}` - Eliminar puesto

### Colaboradores (Employees)

- `GET /api/employees` - Obtener todos los colaboradores
- `GET /api/employees/{id}` - Obtener colaborador por ID
- `GET /api/employees/team/{team}` - Obtener colaboradores por equipo
- `GET /api/employees/position/{positionId}` - Obtener colaboradores por puesto
- `POST /api/employees` - Crear nuevo colaborador
- `PUT /api/employees/{id}` - Actualizar colaborador
- `DELETE /api/employees/{id}` - Eliminar colaborador

## 📝 Ejemplos de Uso

### Crear un Puesto

```bash
curl -X POST http://localhost:8080/api/positions \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Desarrollador Senior",
    "description": "Desarrollador de software con experiencia"
  }'
```

### Crear un Colaborador

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "Juan",
    "lastName": "Pérez",
    "positionId": 1,
    "team": "Desarrollo",
    "tags": ["Java", "Spring Boot", "MySQL"]
  }'
```

## 🧪 Testing

Ejecutar todos los tests:

```bash
mvn test
```

Los tests utilizan H2 in-memory database, por lo que no necesitas tener MySQL corriendo para ejecutarlos.

## 🏢 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/orioncode/backoffice/
│   │   ├── common/exception/        # Manejo global de excepciones
│   │   ├── config/                  # Configuración (OpenAPI, etc.)
│   │   ├── position/                # Módulo de puestos
│   │   ├── employee/                # Módulo de colaboradores
│   │   └── BackofficeApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/orioncode/backoffice/
    │   ├── position/
    │   └── employee/
    └── resources/
        └── application.properties
```

## 🔄 Migración a Microservicios

Cada módulo (position, employee) está diseñado para ser independiente y puede ser extraído fácilmente como microservicio:

1. Copiar el módulo a un nuevo proyecto
2. Agregar su propia configuración de base de datos
3. Agregar dependencias necesarias
4. Configurar el puerto y contexto
5. Desplegar independientemente

## 📄 Licencia

Este proyecto es propiedad de OrionCode.

## 👥 Contribuidores

- OrionCode Team
