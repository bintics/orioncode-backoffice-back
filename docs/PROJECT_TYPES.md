# Tipos de Proyectos

## Descripción

El campo `type` permite categorizar los proyectos según su naturaleza técnica. Esto facilita la organización, búsqueda y análisis de proyectos en el IDP.

## Tipos de Proyectos Disponibles

### 1. **WEB_APPLICATION**
Aplicaciones web tradicionales, portales, dashboards y sistemas web interactivos.

**Ejemplos:**
- E-Commerce Platform
- Customer Portal
- Admin Dashboard
- Support Ticketing System
- Content Management System
- Knowledge Base
- Video Streaming Platform

**Características:**
- Interfaz de usuario basada en navegador
- Frontend (React, Angular, Vue)
- Backend API (REST/GraphQL)
- Base de datos

---

### 2. **MOBILE_APP**
Aplicaciones móviles nativas o híbridas para iOS, Android o multiplataforma.

**Ejemplos:**
- Mobile Banking App
- Aplicaciones de delivery
- Apps de fitness

**Características:**
- Interfaces nativas (Swift, Kotlin)
- Multiplataforma (Flutter, React Native)
- Integración con APIs backend
- Notificaciones push

---

### 3. **MICROSERVICE**
Servicios independientes que forman parte de una arquitectura de microservicios.

**Ejemplos:**
- API Gateway
- Authentication Service
- Payment Service
- Notification Service

**Características:**
- Servicio único y autónomo
- API bien definida
- Base de datos independiente
- Despliegue independiente

---

### 4. **INFRASTRUCTURE**
Proyectos relacionados con infraestructura, DevOps y plataforma.

**Ejemplos:**
- CI/CD Pipeline
- Cloud Migration
- Service Mesh Implementation
- Kubernetes Cluster

**Características:**
- Infraestructura como código (Terraform, CloudFormation)
- Configuración de CI/CD
- Orquestación de contenedores
- Gestión de recursos cloud

---

### 5. **DATA_ENGINEERING**
Proyectos de ingeniería de datos, ETL, data warehouses y análisis.

**Ejemplos:**
- Data Warehouse
- Business Intelligence
- Real-time Analytics
- Data Lake

**Características:**
- Pipelines de datos (Airflow, Kafka)
- Procesamiento batch/streaming
- Almacenamiento de datos (Snowflake, BigQuery)
- Dashboards y reportes

---

### 6. **MACHINE_LEARNING**
Proyectos de inteligencia artificial, machine learning y data science.

**Ejemplos:**
- Recommendation Engine
- Chatbot Platform
- Fraud Detection System
- Image Recognition

**Características:**
- Modelos de ML/DL
- Entrenamiento de modelos
- Inferencia en producción
- MLOps

---

### 7. **SECURITY**
Proyectos enfocados en seguridad, auditoría y cumplimiento.

**Ejemplos:**
- Security Audit System
- Identity Management
- Vulnerability Scanner
- SIEM Platform

**Características:**
- Monitoreo de seguridad
- Gestión de identidades (IAM)
- Auditoría y compliance
- Prevención de amenazas

---

### 8. **INTEGRATION**
Proyectos de integración entre sistemas, APIs y plataformas externas.

**Ejemplos:**
- ERP Integration
- Payment Gateway
- Social Media Integration
- Blockchain Integration

**Características:**
- Conectores y adaptadores
- Transformación de datos
- Orquestación de integraciones
- APIs de terceros

---

### 9. **TOOLING**
Herramientas internas, frameworks y utilidades para desarrolladores.

**Ejemplos:**
- Automated Testing Suite
- Monitoring Dashboard
- Load Testing Framework
- Development CLI Tools

**Características:**
- Herramientas de productividad
- Frameworks internos
- SDKs y librerías
- Scripts de automatización

---

### 10. **IOT**
Proyectos relacionados con Internet of Things y dispositivos conectados.

**Ejemplos:**
- IoT Platform
- Smart Home System
- Fleet Management
- Sensor Network

**Características:**
- Gestión de dispositivos
- Recolección de telemetría
- Edge computing
- Protocolos IoT (MQTT, CoAP)

---

### 11. **ARCHITECTURE**
Proyectos de diseño arquitectónico, migraciones y modernizaciones.

**Ejemplos:**
- Microservices Architecture
- Legacy System Migration
- System Redesign
- Platform Modernization

**Características:**
- Diseño de sistemas
- Documentación arquitectónica
- Patrones y mejores prácticas
- Migraciones complejas

---

## Uso en la API

### Crear Proyecto con Tipo

```bash
curl -X POST "http://localhost:8080/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nuevo Proyecto",
    "description": "Descripción del proyecto",
    "status": "ACTIVE",
    "type": "WEB_APPLICATION",
    "ownerId": "team-001"
  }'
```

### Buscar por Tipo

```bash
# Buscar todos los proyectos de tipo WEB_APPLICATION
curl -X GET "http://localhost:8080/projects?filter=type&search=WEB_APPLICATION"

# Buscar proyectos de infraestructura
curl -X GET "http://localhost:8080/projects?filter=type&search=INFRASTRUCTURE"

# Buscar proyectos de Machine Learning
curl -X GET "http://localhost:8080/projects?filter=type&search=MACHINE_LEARNING"
```

### Listar Proyectos por Tipo

```bash
# Ver distribución de proyectos por tipo
curl -X GET "http://localhost:8080/projects?page=1&size=100" | jq '.data | group_by(.type) | map({type: .[0].type, count: length})'
```

## Validaciones

- El campo `type` es **obligatorio** al crear o actualizar un proyecto
- Máximo 50 caracteres
- Se recomienda usar valores consistentes (ej: usar `SNAKE_CASE`)

## Beneficios

1. **Organización**: Agrupar proyectos por categorías técnicas
2. **Búsqueda**: Encontrar rápidamente proyectos de un tipo específico
3. **Análisis**: Generar métricas por tipo de proyecto
4. **Asignación**: Asignar equipos especializados según el tipo
5. **Plantillas**: Crear templates por tipo de proyecto
6. **Reportes**: Visualizar distribución de proyectos

## Extensibilidad

Puedes agregar nuevos tipos según las necesidades de tu organización:

- **BLOCKCHAIN**: Proyectos de blockchain y DLT
- **GAME_DEVELOPMENT**: Desarrollo de videojuegos
- **EMBEDDED_SYSTEMS**: Sistemas embebidos
- **DESKTOP_APPLICATION**: Aplicaciones de escritorio
- **API**: APIs standalone
- **LIBRARY**: Librerías y SDKs

## Ejemplo de Respuesta

```json
{
  "id": 1,
  "name": "E-Commerce Platform",
  "description": "Plataforma de comercio electrónico escalable",
  "status": "ACTIVE",
  "type": "WEB_APPLICATION",
  "ownerId": "team-001",
  "createdAt": "2023-11-10T09:15:00Z",
  "updatedAt": "2024-05-20T14:30:00Z"
}
```

## Metadata de Búsqueda

La API ahora incluye `type` en los filtros disponibles:

```json
{
  "data": [...],
  "pagination": {...},
  "metadata": {
    "filters": ["name", "description", "status", "type", "ownerId"]
  }
}
```

