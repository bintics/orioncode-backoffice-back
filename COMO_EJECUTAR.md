# Cómo Ejecutar la Aplicación

## Opciones de Ejecución

La aplicación soporta dos perfiles de configuración:

### 1. Perfil de Desarrollo (H2 en memoria) - **RECOMENDADO PARA DESARROLLO**

Este perfil usa H2, una base de datos en memoria que no requiere instalación ni configuración adicional.

#### Ejecutar con Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Ejecutar el JAR:
```bash
mvn clean package
java -jar target/backoffice-1.0.0.jar --spring.profiles.active=dev
```

#### Desde IntelliJ IDEA:
1. Abre la configuración de ejecución (Run > Edit Configurations)
2. En la clase `BackofficeApplication`
3. Agrega en "VM options": `-Dspring.profiles.active=dev`
4. Aplica y ejecuta

**Ventajas del perfil dev:**
- ✅ No requiere MySQL instalado
- ✅ Base de datos se crea automáticamente al iniciar
- ✅ Datos de prueba se cargan desde `data.sql`
- ✅ Consola H2 disponible en: http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:mem:devdb`
  - Username: `sa`
  - Password: (vacío)

### 2. Perfil de Producción (MySQL) - **PARA PRODUCCIÓN**

Este perfil usa MySQL como base de datos.

#### Prerrequisitos:
1. Tener MySQL instalado y corriendo
2. Crear la base de datos (opcional, se crea automáticamente si no existe):
   ```sql
   CREATE DATABASE orioncode_backoffice;
   ```

#### Configurar credenciales (opcional):
Edita `src/main/resources/application.properties` si tus credenciales son diferentes:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/orioncode_backoffice?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

#### Ejecutar con Maven:
```bash
mvn spring-boot:run
```

#### Ejecutar el JAR:
```bash
mvn clean package
java -jar target/backoffice-1.0.0.jar
```

#### Desde IntelliJ IDEA:
Simplemente ejecuta la clase `BackofficeApplication` sin configuración adicional.

## URLs de la Aplicación

Una vez iniciada la aplicación, estará disponible en:

- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/api/api-docs
- **H2 Console** (solo perfil dev): http://localhost:8080/api/h2-console

## Endpoints Principales

### Colaboradores
- `GET /api/collaborators` - Buscar colaboradores (con paginación y filtros)
- `GET /api/collaborators/{id}` - Obtener colaborador por ID
- `POST /api/collaborators` - Crear colaborador
- `PUT /api/collaborators/{id}` - Actualizar colaborador
- `DELETE /api/collaborators/{id}` - Eliminar colaborador

### Posiciones
- `GET /api/positions` - Listar todas las posiciones
- `GET /api/positions/{id}` - Obtener posición por ID
- `POST /api/positions` - Crear posición
- `PUT /api/positions/{id}` - Actualizar posición
- `DELETE /api/positions/{id}` - Eliminar posición

### Equipos
- `GET /api/teams` - Listar todos los equipos
- `GET /api/teams/{id}` - Obtener equipo por ID
- `GET /api/teams/name/{name}` - Obtener equipo por nombre
- `POST /api/teams` - Crear equipo
- `PUT /api/teams/{id}` - Actualizar equipo
- `DELETE /api/teams/{id}` - Eliminar equipo

## Solución de Problemas

### Error: "Failed to configure a DataSource"

**Causa**: MySQL no está corriendo o las credenciales son incorrectas.

**Solución**: 
1. Usa el perfil `dev` (H2) para desarrollo:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
2. O verifica que MySQL esté corriendo:
   ```bash
   # En macOS
   brew services start mysql
   
   # O verifica el estado
   brew services list | grep mysql
   ```

### Error: "Access denied for user"

**Causa**: Credenciales de MySQL incorrectas.

**Solución**: Actualiza las credenciales en `application.properties` o usa el perfil `dev`.

### Puerto 8080 ya en uso

**Causa**: Otro proceso está usando el puerto 8080.

**Solución**: 
1. Cambia el puerto en `application.properties`:
   ```properties
   server.port=8081
   ```
2. O mata el proceso que usa el puerto:
   ```bash
   lsof -ti:8080 | xargs kill -9
   ```

## Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest=CollaboratorServiceTest

# Ejecutar tests con reporte de cobertura
mvn clean test jacoco:report
```

## Compilar sin ejecutar tests

```bash
mvn clean package -DskipTests
```

## Datos de Prueba

Cuando ejecutas con el perfil `dev`, la aplicación carga automáticamente datos de prueba desde `src/main/resources/data.sql`:
- 20 posiciones
- 15 equipos  
- 140 colaboradores de ejemplo
- Tags asociados

Para cargar más datos, ejecuta el script SQL completo desde DBeaver o MySQL Workbench.

