#!/bin/bash

# Script para probar el endpoint POST /positions/batch
# Este endpoint permite obtener múltiples puestos por sus IDs en una sola petición

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "TEST: Obtener múltiples puestos por IDs"
echo "=========================================="
echo ""

# Ejemplo 1: Obtener 3 puestos específicos
echo "1. Obtener 3 puestos por sus IDs:"
echo "   POST $BASE_URL/positions/batch"
echo ""
curl -X POST "$BASE_URL/positions/batch" \
  -H "Content-Type: application/json" \
  -d '["pos-001", "pos-002", "pos-003"]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 2: Obtener múltiples puestos (incluyendo algunos que no existen)
echo "2. Obtener puestos con IDs válidos e inválidos (los inválidos se ignoran):"
echo "   POST $BASE_URL/positions/batch"
echo ""
curl -X POST "$BASE_URL/positions/batch" \
  -H "Content-Type: application/json" \
  -d '["pos-001", "invalid-id", "pos-005", "another-invalid"]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 3: Array vacío
echo "3. Enviar array vacío:"
echo "   POST $BASE_URL/positions/batch"
echo ""
curl -X POST "$BASE_URL/positions/batch" \
  -H "Content-Type: application/json" \
  -d '[]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 4: Obtener muchos puestos (útil para demostrar que no hay límite de URL)
echo "4. Obtener 10 puestos en una sola petición:"
echo "   POST $BASE_URL/positions/batch"
echo ""
curl -X POST "$BASE_URL/positions/batch" \
  -H "Content-Type: application/json" \
  -d '["pos-001", "pos-002", "pos-003", "pos-004", "pos-005", "pos-006", "pos-007", "pos-008", "pos-009", "pos-010"]' \
  | jq .

echo ""
echo "=========================================="
echo "Tests completados"
echo "=========================================="

