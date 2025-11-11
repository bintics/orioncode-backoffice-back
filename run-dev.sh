#!/bin/bash

echo "================================================"
echo "  OrionCode Backoffice - Modo Desarrollo (H2)"
echo "================================================"
echo ""
echo "Iniciando aplicación con perfil 'dev'..."
echo "Base de datos: H2 en memoria"
echo ""

mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo ""
echo "Aplicación detenida."

