#!/bin/bash

# Script para probar el endpoint POST /teams/batch
# Este endpoint permite obtener múltiples equipos por sus IDs en una sola petición

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "TEST: Obtener múltiples equipos por IDs"
echo "=========================================="
echo ""

# Ejemplo 1: Obtener 3 equipos específicos
echo "1. Obtener 3 equipos por sus IDs:"
echo "   POST $BASE_URL/teams/batch"
echo ""
curl -X POST "$BASE_URL/teams/batch" \
  -H "Content-Type: application/json" \
  -d '["team-001", "team-002", "team-003"]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 2: Obtener múltiples equipos (incluyendo algunos que no existen)
echo "2. Obtener equipos con IDs válidos e inválidos (los inválidos se ignoran):"
echo "   POST $BASE_URL/teams/batch"
echo ""
curl -X POST "$BASE_URL/teams/batch" \
  -H "Content-Type: application/json" \
  -d '["team-001", "invalid-id", "team-005", "another-invalid"]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 3: Array vacío
echo "3. Enviar array vacío:"
echo "   POST $BASE_URL/teams/batch"
echo ""
curl -X POST "$BASE_URL/teams/batch" \
  -H "Content-Type: application/json" \
  -d '[]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 4: Obtener muchos equipos (útil para demostrar que no hay límite de URL)
echo "4. Obtener 10 equipos en una sola petición:"
echo "   POST $BASE_URL/teams/batch"
echo ""
curl -X POST "$BASE_URL/teams/batch" \
  -H "Content-Type: application/json" \
  -d '["team-001", "team-002", "team-003", "team-004", "team-005", "team-006", "team-007", "team-008", "team-009", "team-010"]' \
  | jq .

echo ""
echo "=========================================="
echo "Tests completados"
echo "=========================================="

