# Solución al Error: "Failed to configure a DataSource"

## 🔴 Problema Original

Al intentar ejecutar la aplicación, aparecía el siguiente error:

```
APPLICATION FAILED TO START
Description: Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
Reason: Failed to determine a suitable driver class
```

## 🔍 Causas del Error

Este error ocurre cuando:

1. **MySQL no está corriendo** o no es accesible
2. Las **credenciales de conexión son incorrectas**
3. No hay una **base de datos alternativa configurada** para desarrollo
4. El archivo de configuración tiene **errores de sintaxis**

## ✅ Soluciones Implementadas

### 1. Perfil de Desarrollo con H2 (Solución Principal)

Se creó un perfil `dev` que usa H2 en memoria, eliminando la dependencia de MySQL para desarrollo:

**Archivo creado:** `src/main/resources/application-dev.properties`

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:devdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console habilitada
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Carga automática de datos de prueba
spring.sql.init.mode=always
```

**Cómo usar:**
```bash
# Con script
./run-dev.sh

# O con Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Desde IntelliJ:**
- Run > Edit Configurations
- VM options: `-Dspring.profiles.active=dev`

### 2. Actualización de Dependencias

Se modificó el `pom.xml` para que H2 esté disponible en runtime (no solo en tests):

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>  <!-- Cambiado de test a runtime -->
</dependency>
```

### 3. Corrección del application.properties

Se corrigió un error de sintaxis en el archivo principal de configuración (comentario con // en lugar de #).

### 4. Script de Inicio Rápido

Se creó `run-dev.sh` para facilitar el inicio en modo desarrollo:

```bash
chmod +x run-dev.sh
./run-dev.sh
```

## 📋 Ventajas del Perfil Dev

✅ **No requiere instalación de MySQL**
✅ **Configuración cero** - funciona inmediatamente
✅ **Datos de prueba automáticos** - carga desde data.sql
✅ **Consola H2 integrada** - para ver/modificar datos
✅ **Reinicio rápido** - base de datos en memoria
✅ **Perfecto para desarrollo** y pruebas locales

## 🗂️ Archivos Creados/Modificados

### Nuevos Archivos:
1. `src/main/resources/application-dev.properties` - Configuración perfil dev
2. `run-dev.sh` - Script de inicio rápido
3. `COMO_EJECUTAR.md` - Guía completa de ejecución
4. `SOLUCION_ERROR_DATASOURCE.md` - Este archivo

### Archivos Modificados:
1. `pom.xml` - Cambio de scope de H2
2. `src/main/resources/application.properties` - Corrección sintaxis
3. `README.md` - Actualización con instrucciones

## 🎯 Cómo Ejecutar Ahora

### Para Desarrollo (Recomendado):
```bash
./run-dev.sh
```

### Para Producción (con MySQL):
```bash
# 1. Asegúrate de tener MySQL corriendo
brew services start mysql

# 2. Ejecuta la aplicación
mvn spring-boot:run
```

## 🌐 URLs Disponibles

Una vez iniciada la aplicación:

- **API Base**: http://localhost:8080/api
- **Swagger/OpenAPI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console** (solo dev): http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:mem:devdb`
  - Username: `sa`
  - Password: (vacío)

## 🧪 Verificar que Funciona

```bash
# Verificar que la aplicación está corriendo
curl http://localhost:8080/api/positions

# Debería retornar un array JSON con las posiciones de prueba
```

## 📖 Más Información

Para información detallada sobre:
- Configuración de MySQL
- Variables de entorno
- Perfiles adicionales
- Solución de otros problemas

Consulta: [COMO_EJECUTAR.md](COMO_EJECUTAR.md)

## 💡 Recomendaciones

1. **Usa el perfil `dev` para desarrollo local** - es más rápido y no requiere configuración
2. **Usa MySQL solo para producción** o cuando necesites persistencia entre reinicios
3. **Revisa los logs** si sigues teniendo problemas
4. **Verifica la versión de Java** (debe ser 17+):
   ```bash
   java -version
   ```

## 🆘 Si el Problema Persiste

1. Limpia el proyecto:
   ```bash
   mvn clean
   ```

2. Recompila:
   ```bash
   mvn clean compile
   ```

3. Verifica que no haya otros procesos usando el puerto 8080:
   ```bash
   lsof -ti:8080
   ```

4. Revisa los logs completos en la terminal para más detalles del error

