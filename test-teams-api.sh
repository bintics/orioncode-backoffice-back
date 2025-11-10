#!/bin/bash

# Script para probar el API de Teams
# Uso: bash test-teams-api.sh

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "Prueba del API de Teams"
echo "=========================================="
echo ""

# 1. Crear un equipo
echo "1. Creando equipo 'Backend Team'..."
TEAM1_RESPONSE=$(curl -s -X POST "$BASE_URL/teams" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Team",
    "description": "Equipo de desarrollo backend"
  }')

TEAM1_ID=$(echo $TEAM1_RESPONSE | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "   ✓ Equipo creado con ID: $TEAM1_ID"
echo "   Respuesta: $TEAM1_RESPONSE"
echo ""

# 2. Crear otro equipo
echo "2. Creando equipo 'Frontend Team'..."
TEAM2_RESPONSE=$(curl -s -X POST "$BASE_URL/teams" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Frontend Team",
    "description": "Equipo de desarrollo frontend"
  }')

TEAM2_ID=$(echo $TEAM2_RESPONSE | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "   ✓ Equipo creado con ID: $TEAM2_ID"
echo ""

# 3. Listar todos los equipos
echo "3. Listando todos los equipos..."
curl -s "$BASE_URL/teams" | python3 -m json.tool
echo ""

# 4. Obtener equipo por ID
echo "4. Obteniendo equipo por ID ($TEAM1_ID)..."
curl -s "$BASE_URL/teams/$TEAM1_ID" | python3 -m json.tool
echo ""

# 5. Obtener equipo por nombre
echo "5. Obteniendo equipo por nombre (Backend Team)..."
curl -s "$BASE_URL/teams/name/Backend%20Team" | python3 -m json.tool
echo ""

# 6. Actualizar equipo
echo "6. Actualizando equipo ($TEAM1_ID)..."
curl -s -X PUT "$BASE_URL/teams/$TEAM1_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Development Team",
    "description": "Equipo de desarrollo backend - ACTUALIZADO"
  }' | python3 -m json.tool
echo ""

# 7. Crear una posición
echo "7. Creando posición 'Senior Developer'..."
POSITION_RESPONSE=$(curl -s -X POST "$BASE_URL/positions" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Senior Developer",
    "description": "Desarrollador senior"
  }')

POSITION_ID=$(echo $POSITION_RESPONSE | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "   ✓ Posición creada con ID: $POSITION_ID"
echo ""

# 8. Crear un colaborador
echo "8. Creando colaborador..."
EMPLOYEE_RESPONSE=$(curl -s -X POST "$BASE_URL/collaborators" \
  -H "Content-Type: application/json" \
  -d "{
    \"firstName\": \"Juan\",
    \"lastName\": \"Pérez\",
    \"positionId\": \"$POSITION_ID\",
    \"team\": \"Backend Development Team\",
    \"tags\": [\"Java\", \"Spring Boot\", \"Microservices\"]
  }")

EMPLOYEE_ID=$(echo $EMPLOYEE_RESPONSE | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "   ✓ Colaborador creado con ID: $EMPLOYEE_ID"
echo "   Respuesta: $EMPLOYEE_RESPONSE"
echo ""

# 9. Listar colaboradores por equipo
echo "9. Listando colaboradores del equipo 'Backend Development Team'..."
curl -s "$BASE_URL/collaborators/team/Backend%20Development%20Team" | python3 -m json.tool
echo ""

# 10. Eliminar equipo (comentado para no afectar los datos)
# echo "10. Eliminando equipo ($TEAM2_ID)..."
# curl -s -X DELETE "$BASE_URL/teams/$TEAM2_ID"
# echo "   ✓ Equipo eliminado"
# echo ""

echo "=========================================="
echo "Pruebas completadas exitosamente!"
echo "=========================================="
echo ""
echo "IDs generados en esta prueba:"
echo "  - Team 1: $TEAM1_ID"
echo "  - Team 2: $TEAM2_ID"
echo "  - Position: $POSITION_ID"
echo "  - Employee: $EMPLOYEE_ID"
echo ""
echo "Nota: Si deseas eliminar el equipo 2, ejecuta:"
echo "  curl -X DELETE $BASE_URL/teams/$TEAM2_ID"

