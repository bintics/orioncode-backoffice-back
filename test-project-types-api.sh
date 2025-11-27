#!/bin/bash

# Script para probar la API de tipos de proyecto
BASE_URL="http://localhost:8080/project-types"

echo "========================================="
echo "Probando API de Tipos de Proyecto"
echo "========================================="

# 1. Listar todos los tipos de proyecto
echo -e "\n1. Listando todos los tipos de proyecto..."
curl -X GET "$BASE_URL/all" | jq .

# 2. Buscar tipos con paginación
echo -e "\n2. Listando tipos con paginación..."
curl -X GET "$BASE_URL?page=1&size=5" | jq .

# 3. Buscar tipo por nombre
echo -e "\n3. Buscando tipo por nombre 'WEB'..."
curl -X GET "$BASE_URL?filter=name&search=WEB" | jq .

# 4. Obtener tipo por ID
echo -e "\n4. Obteniendo tipo de proyecto por ID..."
curl -X GET "$BASE_URL/1" | jq .

# 5. Obtener tipo por nombre
echo -e "\n5. Obteniendo tipo por nombre 'MOBILE_APP'..."
curl -X GET "$BASE_URL/name/MOBILE_APP" | jq .

# 6. Crear nuevo tipo
echo -e "\n6. Creando nuevo tipo de proyecto..."
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "BLOCKCHAIN",
    "description": "Proyectos relacionados con blockchain y tecnologías distribuidas"
  }' | jq .

# 7. Actualizar tipo
echo -e "\n7. Actualizando tipo de proyecto..."
curl -X PUT "$BASE_URL/pt-001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "WEB_APPLICATION",
    "description": "Aplicaciones web modernas con tecnologías front y backend actualizadas"
  }' | jq .

# 8. Buscar por descripción
echo -e "\n8. Buscando tipos que contengan 'infraestructura'..."
curl -X GET "$BASE_URL?filter=description&search=infraestructura" | jq .

# 9. Listar ordenados por nombre descendente
echo -e "\n9. Listando tipos ordenados por nombre (desc)..."
curl -X GET "$BASE_URL?page=1&size=5&sortBy=name&sortDir=desc" | jq .

echo -e "\n========================================="
echo "Pruebas completadas"
echo "========================================="

