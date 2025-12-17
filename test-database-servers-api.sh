#!/bin/bash

# Script de prueba para los endpoints de Database Servers
# Asegúrate de que el servidor esté corriendo antes de ejecutar este script

BASE_URL="http://localhost:8080/database-servers"

echo "=========================================="
echo "PRUEBAS DE API - DATABASE SERVERS"
echo "=========================================="
echo ""

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Crear un servidor PostgreSQL
echo -e "${YELLOW}1. Crear servidor PostgreSQL${NC}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "db-srv-test-001",
    "name": "PostgreSQL Production Server",
    "engine": "postgresql",
    "version": "15.2",
    "host": "db-prod-01.example.com",
    "port": 5432,
    "environment": "production",
    "description": "Servidor principal de producción"
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 2. Crear un servidor MySQL
echo -e "${YELLOW}2. Crear servidor MySQL${NC}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "db-srv-test-002",
    "name": "MySQL Development Server",
    "engine": "mysql",
    "version": "8.0.35",
    "host": "db-dev-01.example.com",
    "port": 3306,
    "environment": "development",
    "description": "Servidor de desarrollo"
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 3. Crear un servidor Oracle
echo -e "${YELLOW}3. Crear servidor Oracle${NC}"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "db-srv-test-003",
    "name": "Oracle QA Server",
    "engine": "oracle",
    "version": "19c",
    "host": "db-qa-01.example.com",
    "port": 1521,
    "environment": "qa",
    "description": "Servidor Oracle para QA"
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 4. Obtener servidor por ID
echo -e "${YELLOW}4. Obtener servidor por ID (db-srv-test-001)${NC}"
curl -X GET "$BASE_URL/db-srv-test-001" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 5. Buscar todos los servidores (sin filtro)
echo -e "${YELLOW}5. Buscar todos los servidores (página 1, tamaño 10)${NC}"
curl -X GET "$BASE_URL?page=1&size=10" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 6. Buscar servidores por engine
echo -e "${YELLOW}6. Buscar servidores por engine (postgresql)${NC}"
curl -X GET "$BASE_URL?filter=engine&search=postgresql" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 7. Buscar servidores por environment
echo -e "${YELLOW}7. Buscar servidores por environment (production)${NC}"
curl -X GET "$BASE_URL?filter=environment&search=production" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 8. Búsqueda general (busca en todos los campos)
echo -e "${YELLOW}8. Búsqueda general (search=dev)${NC}"
curl -X GET "$BASE_URL?search=dev&page=1&size=5" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 9. Actualizar servidor
echo -e "${YELLOW}9. Actualizar servidor (db-srv-test-002)${NC}"
curl -X PUT "$BASE_URL/db-srv-test-002" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MySQL Development Server (Updated)",
    "engine": "mysql",
    "version": "8.0.36",
    "host": "db-dev-02.example.com",
    "port": 3306,
    "environment": "development",
    "description": "Servidor de desarrollo actualizado",
    "active": true
  }' \
  -w "\nStatus: %{http_code}\n\n"

# 10. Verificar actualización
echo -e "${YELLOW}10. Verificar actualización (db-srv-test-002)${NC}"
curl -X GET "$BASE_URL/db-srv-test-002" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 11. Buscar con ordenamiento descendente
echo -e "${YELLOW}11. Buscar con ordenamiento por nombre (desc)${NC}"
curl -X GET "$BASE_URL?page=1&size=10&sortBy=name&sortDir=desc" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 12. Eliminar servidor
echo -e "${YELLOW}12. Eliminar servidor (db-srv-test-003)${NC}"
curl -X DELETE "$BASE_URL/db-srv-test-003" \
  -w "\nStatus: %{http_code}\n\n"

# 13. Verificar que el servidor fue eliminado
echo -e "${YELLOW}13. Intentar obtener servidor eliminado (db-srv-test-003)${NC}"
curl -X GET "$BASE_URL/db-srv-test-003" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 14. Búsqueda por host
echo -e "${YELLOW}14. Buscar por host (db-prod)${NC}"
curl -X GET "$BASE_URL?filter=host&search=db-prod" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo -e "${GREEN}=========================================="
echo "PRUEBAS COMPLETADAS"
echo -e "==========================================${NC}"
echo ""
echo "Nota: Los servidores db-srv-test-001 y db-srv-test-002 aún existen en la BD."
echo "Puedes eliminarlos manualmente si lo deseas."

