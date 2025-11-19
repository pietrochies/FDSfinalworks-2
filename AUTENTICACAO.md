# Sistema de Autenticação - Guia de Uso

## 📋 Resumo da Implementação

O sistema agora possui autenticação e registro de usuários com:

✅ **Tipos de Usuário:**
- `MASTER`: Administrador (pré-cadastrado)
- `CLIENTE`: Usuário comum (cadastro aberto)

✅ **Recursos Implementados:**
- Registro de novos clientes (sem autenticação)
- Login de usuários
- Logout
- Verificação de sessão autenticada
- Acesso público ao cardápio (sem autenticação)

---

## 🔑 Credenciais Padrão

### Usuário MASTER
- **Email:** `master@lancheria.com`
- **Senha:** `master123`
- **Tipo:** Administrador

---

## 📡 Endpoints de Autenticação

### 1. Registrar Novo Cliente
```
POST /auth/registro
Content-Type: application/json

{
  "email": "cliente@email.com",
  "senha": "senha123",
  "nome": "Nome do Cliente"
}
```

**Respostas:**
- `201 Created`: Sucesso
- `400 Bad Request`: Email duplicado ou dados inválidos

---

### 2. Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "master@lancheria.com",
  "senha": "master123"
}
```

**Respostas:**
- `200 OK`: Login bem-sucedido
- `401 Unauthorized`: Email ou senha incorretos

---

### 3. Logout
```
POST /auth/logout
```

**Respostas:**
- `200 OK`: Logout realizado

---

### 4. Verificar Autenticação
```
GET /auth/verificar
```

**Resposta:**
```json
{
  "autenticado": true/false
}
```

---

### 5. Obter Usuário Autenticado
```
GET /auth/usuario
```

**Respostas:**
- `200 OK`: Retorna dados do usuário
- `401 Unauthorized`: Nenhum usuário autenticado

---

### 6. Acessar Cardápio (SEM autenticação)
```
GET /cardapios
GET /cardapios/1
```

**Nota:** Esses endpoints funcionam mesmo SEM autenticação

---

## 🧪 Como Testar

### Opção 1: Script Bash
```bash
cd /workspaces/FDSfinalworks-2
chmod +x test_auth.sh
./test_auth.sh
```

### Opção 2: HTTP Client (VS Code)
Abra o arquivo `src/main/resources/auth-requests.http` e use a extensão REST Client para fazer as requisições.

### Opção 3: cURL Manual
```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"master@lancheria.com","senha":"master123"}'

# Registrar
curl -X POST http://localhost:8080/auth/registro \
  -H "Content-Type: application/json" \
  -d '{"email":"novo@email.com","senha":"senha123","nome":"João"}'

# Verificar autenticação
curl http://localhost:8080/auth/verificar

# Acessar cardápio (sem autenticação)
curl http://localhost:8080/cardapios
```

---

## 🗄️ Estrutura do Banco de Dados

### Tabela: `usuarios`
```sql
CREATE TABLE usuarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  senha VARCHAR(255) NOT NULL,
  nome VARCHAR(255) NOT NULL,
  tipo_usuario VARCHAR(20) NOT NULL, -- MASTER ou CLIENTE
  ativo BOOLEAN NOT NULL DEFAULT TRUE
);
```

---

## 🔐 Notas de Segurança

⚠️ **IMPORTANTE PARA PRODUÇÃO:**
- Atualmente as senhas são armazenadas em **texto plano** (NUNCA fazer isso em produção!)
- Implemente **hashing com BCrypt** ou **Argon2**
- Use **HTTPS** em produção
- Implemente **CSRF protection**
- Use **tokens JWT** em vez de sessões HTTP

---

## 📝 Exemplo de Fluxo

```
1. Cliente acessa o cardápio (SEM autenticação)
   GET /cardapios → 200 OK

2. Cliente se registra
   POST /auth/registro → 201 Created

3. Cliente faz login
   POST /auth/login → 200 OK (sessão iniciada)

4. Cliente acessa casos de uso protegidos
   GET /pedidos → 200 OK (autenticado)

5. Cliente faz logout
   POST /auth/logout → 200 OK (sessão destruída)

6. Cliente tenta acessar rota protegida
   GET /pedidos → 401 Unauthorized (não autenticado)
```

---

## ✨ Próximas Melhorias

- [ ] Hash de senhas com BCrypt
- [ ] Tokens JWT
- [ ] Rate limiting
- [ ] Verificação de email
- [ ] Reset de senha
- [ ] Proteção contra brute force
- [ ] Logs de auditoria
