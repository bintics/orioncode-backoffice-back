#!/bin/bash

# Script para probar la API de proyectos
BASE_URL="http://localhost:8080/projects"

echo "========================================="
echo "Probando API de Proyectos"
echo "========================================="

# 1. Crear un proyecto
echo -e "\n1. Creando proyecto..."
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Proyecto Test",
    "description": "Proyecto de prueba desde script",
    "status": "ACTIVE",
    "ownerId": "team-001"
  }' | jq .

# 2. Listar proyectos con paginación
echo -e "\n2. Listando proyectos..."
curl -X GET "$BASE_URL?page=1&size=5" | jq .

# 3. Buscar proyecto por nombre
echo -e "\n3. Buscando proyectos por nombre..."
curl -X GET "$BASE_URL?filter=name&search=E-Commerce" | jq .

# 4. Buscar proyecto por status
echo -e "\n4. Buscando proyectos activos..."
curl -X GET "$BASE_URL?filter=status&search=ACTIVE&size=3" | jq .

# 5. Obtener proyecto por ID
echo -e "\n5. Obteniendo proyecto por ID..."
curl -X GET "$BASE_URL/1" | jq .

# 6. Actualizar proyecto
echo -e "\n6. Actualizando proyecto..."
curl -X PUT "$BASE_URL/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "E-Commerce Platform Updated",
    "description": "Plataforma actualizada",
    "status": "ACTIVE",
    "type": "WEB_APPLICATION",
    "ownerId": "team-001"
  }' | jq .

echo -e "\n========================================="
echo "Pruebas completadas"
echo "========================================="

