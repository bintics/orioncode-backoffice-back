#!/bin/bash

# Script para probar el endpoint POST /project-types/batch
# Este endpoint permite obtener múltiples tipos de proyecto por sus IDs en una sola petición

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "TEST: Obtener múltiples tipos de proyecto por IDs"
echo "=========================================="
echo ""

# Ejemplo 1: Obtener 3 tipos de proyecto específicos
echo "1. Obtener 3 tipos de proyecto por sus IDs:"
echo "   POST $BASE_URL/project-types/batch"
echo ""
curl -X POST "$BASE_URL/project-types/batch" \
  -H "Content-Type: application/json" \
  -d '["pt-001", "pt-002", "pt-003"]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 2: Obtener múltiples tipos de proyecto (incluyendo algunos que no existen)
echo "2. Obtener tipos de proyecto con IDs válidos e inválidos (los inválidos se ignoran):"
echo "   POST $BASE_URL/project-types/batch"
echo ""
curl -X POST "$BASE_URL/project-types/batch" \
  -H "Content-Type: application/json" \
  -d '["pt-001", "invalid-id", "pt-005", "another-invalid"]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 3: Array vacío
echo "3. Enviar array vacío:"
echo "   POST $BASE_URL/project-types/batch"
echo ""
curl -X POST "$BASE_URL/project-types/batch" \
  -H "Content-Type: application/json" \
  -d '[]' \
  | jq .

echo ""
echo "=========================================="
echo ""

# Ejemplo 4: Obtener muchos tipos de proyecto (útil para demostrar que no hay límite de URL)
echo "4. Obtener 10 tipos de proyecto en una sola petición:"
echo "   POST $BASE_URL/project-types/batch"
echo ""
curl -X POST "$BASE_URL/project-types/batch" \
  -H "Content-Type: application/json" \
  -d '["pt-001", "pt-002", "pt-003", "pt-004", "pt-005", "pt-006", "pt-007", "pt-008", "pt-009", "pt-010"]' \
  | jq .

echo ""
echo "=========================================="
echo "Tests completados"
echo "=========================================="

