#!/bin/bash

# Script de prueba para los endpoints de Database Schemas
# Asegúrate de que el servidor esté corriendo antes de ejecutar este script

BASE_URL="http://localhost:8080/database-schemas"

echo "=========================================="
echo "PRUEBAS DE API - DATABASE SCHEMAS"
echo "=========================================="
echo ""

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Crear un schema de PostgreSQL
echo -e "${YELLOW}1. Crear schema de PostgreSQL${NC}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "schema-test-001",
    "name": "test_customer_portal",
    "serverId": "db-srv-001",
    "description": "Schema de prueba para portal de clientes",
    "owner": "pg_test_admin",
    "tablesCount": 45,
    "viewsCount": 18,
    "proceduresCount": 25,
    "functionsCount": 20,
    "sizeInMB": 3200.75
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 2. Crear un schema de Oracle
echo -e "${YELLOW}2. Crear schema de Oracle (Banking Core)${NC}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "schema-test-002",
    "name": "test_banking_core",
    "serverId": "db-srv-004",
    "description": "Schema de prueba para core bancario",
    "owner": "oracle_test_admin",
    "tablesCount": 280,
    "viewsCount": 95,
    "proceduresCount": 180,
    "functionsCount": 140,
    "sizeInMB": 35000.60
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 3. Crear un schema de MySQL
echo -e "${YELLOW}3. Crear schema de MySQL${NC}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "schema-test-003",
    "name": "test_mobile_app",
    "serverId": "db-srv-003",
    "description": "Schema de prueba para app móvil",
    "owner": "mysql_test_admin",
    "tablesCount": 68,
    "viewsCount": 25,
    "proceduresCount": 42,
    "functionsCount": 35,
    "sizeInMB": 5800.45
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 4. Obtener schema por ID
echo -e "${YELLOW}4. Obtener schema por ID (schema-test-001)${NC}"
curl -X GET "$BASE_URL/schema-test-001" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 5. Buscar todos los schemas (sin filtro)
echo -e "${YELLOW}5. Buscar todos los schemas (página 1, tamaño 10)${NC}"
curl -X GET "$BASE_URL?page=1&size=10" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 6. Buscar schemas por serverId
echo -e "${YELLOW}6. Buscar schemas por serverId (db-srv-001)${NC}"
curl -X GET "$BASE_URL?filter=serverId&search=db-srv-001" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 7. Buscar schemas por owner
echo -e "${YELLOW}7. Buscar schemas por owner (pg_admin)${NC}"
curl -X GET "$BASE_URL?filter=owner&search=pg_admin" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 8. Búsqueda general (busca en todos los campos)
echo -e "${YELLOW}8. Búsqueda general (search=banking)${NC}"
curl -X GET "$BASE_URL?search=banking&page=1&size=5" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 9. Buscar schemas ordenados por tamaño (descendente)
echo -e "${YELLOW}9. Buscar schemas ordenados por tamaño (desc)${NC}"
curl -X GET "$BASE_URL?page=1&size=5&sortBy=sizeInMB&sortDir=desc" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 10. Buscar schemas ordenados por número de tablas
echo -e "${YELLOW}10. Buscar schemas ordenados por número de tablas (desc)${NC}"
curl -X GET "$BASE_URL?page=1&size=5&sortBy=tablesCount&sortDir=desc" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 11. Actualizar schema
echo -e "${YELLOW}11. Actualizar schema (schema-test-001)${NC}"
curl -X PUT "$BASE_URL/schema-test-001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "test_customer_portal_v2",
    "serverId": "db-srv-001",
    "description": "Schema actualizado para portal de clientes versión 2",
    "owner": "pg_test_admin",
    "tablesCount": 52,
    "viewsCount": 22,
    "proceduresCount": 30,
    "functionsCount": 25,
    "sizeInMB": 3850.90
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 12. Verificar actualización
echo -e "${YELLOW}12. Verificar actualización (schema-test-001)${NC}"
curl -X GET "$BASE_URL/schema-test-001" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 13. Buscar schemas de servidor Oracle
echo -e "${YELLOW}13. Buscar schemas de servidor Oracle (db-srv-004)${NC}"
curl -X GET "$BASE_URL?filter=serverId&search=db-srv-004" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 14. Buscar por nombre
echo -e "${YELLOW}14. Buscar por nombre (test_)${NC}"
curl -X GET "$BASE_URL?filter=name&search=test_" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 15. Eliminar schema
echo -e "${YELLOW}15. Eliminar schema (schema-test-003)${NC}"
curl -X DELETE "$BASE_URL/schema-test-003" \
  -w "\nStatus: %{http_code}\n\n"

# 16. Verificar que el schema fue eliminado
echo -e "${YELLOW}16. Intentar obtener schema eliminado (schema-test-003)${NC}"
curl -X GET "$BASE_URL/schema-test-003" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 17. Buscar schemas con paginación
echo -e "${YELLOW}17. Buscar schemas - página 2, tamaño 5${NC}"
curl -X GET "$BASE_URL?page=2&size=5&sortBy=name&sortDir=asc" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 18. Buscar por descripción
echo -e "${YELLOW}18. Buscar por descripción (core)${NC}"
curl -X GET "$BASE_URL?filter=description&search=core" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo -e "${GREEN}=========================================="
echo "PRUEBAS COMPLETADAS"
echo -e "==========================================${NC}"
echo ""
echo "Nota: Los schemas schema-test-001 y schema-test-002 aún existen en la BD."
echo "Puedes eliminarlos manualmente si lo deseas."

