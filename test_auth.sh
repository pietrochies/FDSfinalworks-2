#!/bin/bash

echo "========================================="
echo "TESTES DE AUTENTICAÇÃO - ENDPOINTS"
echo "========================================="
echo ""

# Teste 1: Verificar autenticação inicial (deve estar desautenticado)
echo "✅ 1. Verificar autenticação inicial"
echo "GET /auth/verificar"
curl -s http://localhost:8080/auth/verificar | jq .
echo ""

# Teste 2: Login com usuário MASTER pré-cadastrado
echo "✅ 2. Login com MASTER"
echo "POST /auth/login (master@lancheria.com / master123)"
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"master@lancheria.com","senha":"master123"}' | jq .
echo ""

# Teste 3: Registrar novo usuário cliente
echo "✅ 3. Registrar novo usuário CLIENTE"
echo "POST /auth/registro"
curl -s -X POST http://localhost:8080/auth/registro \
  -H "Content-Type: application/json" \
  -d '{"email":"cliente@email.com","senha":"senha123","nome":"João Cliente"}' | jq .
echo ""

# Teste 4: Login com usuário cliente
echo "✅ 4. Login com CLIENTE"
echo "POST /auth/login (cliente@email.com / senha123)"
RESPONSE=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"cliente@email.com","senha":"senha123"}')
echo "$RESPONSE" | jq .
echo ""

# Teste 5: Tentar login com email incorreto
echo "✅ 5. Tentar login com email incorreto"
echo "POST /auth/login (naoexiste@email.com / password)"
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"naoexiste@email.com","senha":"password"}' | jq .
echo ""

# Teste 6: Tentar registrar com email duplicado
echo "✅ 6. Tentar registrar com email já cadastrado"
echo "POST /auth/registro (master@lancheria.com)"
curl -s -X POST http://localhost:8080/auth/registro \
  -H "Content-Type: application/json" \
  -d '{"email":"master@lancheria.com","senha":"password123","nome":"Outro Admin"}' | jq .
echo ""

# Teste 7: Acessar cardápio SEM autenticação (deve funcionar)
echo "✅ 7. Acessar cardápio SEM autenticação (DEVE FUNCIONAR)"
echo "GET /cardapios"
curl -s http://localhost:8080/cardapios | jq . 2>/dev/null || curl -s http://localhost:8080/cardapios
echo ""

# Teste 8: Logout
echo "✅ 8. Logout"
echo "POST /auth/logout"
curl -s -X POST http://localhost:8080/auth/logout | jq .
echo ""

# Teste 9: Verificar autenticação após logout
echo "✅ 9. Verificar autenticação após logout"
echo "GET /auth/verificar"
curl -s http://localhost:8080/auth/verificar | jq .
echo ""

echo "========================================="
echo "TESTES CONCLUÍDOS"
echo "========================================="
