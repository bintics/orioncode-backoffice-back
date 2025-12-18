#!/bin/bash

BASE_URL="http://localhost:8080"
API_URL="${BASE_URL}/database-objects"

echo "=== Testing Database Objects API ==="
echo ""

# Test 1: Create a new database object
echo "1. Creating a new database object..."
curl -X POST "${API_URL}" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "dbo-001",
    "name": "users",
    "type": "TABLE",
    "schemaId": "schema-001",
    "owner": "admin",
    "description": "Tabla de usuarios del sistema",
    "rowCount": 15000,
    "sizeInKB": 2048.5
  }'
echo -e "\n"

# Test 2: Get all database objects with pagination
echo "2. Getting all database objects (page 1, size 10)..."
curl -X GET "${API_URL}?page=1&size=10"
echo -e "\n"

# Test 3: Search database objects by name
echo "3. Searching database objects by name..."
curl -X GET "${API_URL}?filter=name&search=users"
echo -e "\n"

# Test 4: Search database objects by type
echo "4. Searching database objects by type..."
curl -X GET "${API_URL}?filter=type&search=TABLE"
echo -e "\n"

# Test 5: Get database object by ID
echo "5. Getting database object by ID (dbo-001)..."
curl -X GET "${API_URL}/dbo-001"
echo -e "\n"

# Test 6: Update database object
echo "6. Updating database object..."
curl -X PUT "${API_URL}/dbo-001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "users_updated",
    "type": "TABLE",
    "schemaId": "schema-001",
    "owner": "admin",
    "description": "Tabla de usuarios del sistema actualizada",
    "rowCount": 16000,
    "sizeInKB": 2100.0
  }'
echo -e "\n"

# Test 7: Delete database object
echo "7. Deleting database object..."
curl -X DELETE "${API_URL}/dbo-001"
echo -e "\n"

echo "=== Tests completed ==="

