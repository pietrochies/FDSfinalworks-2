# 🛠️ Endpoints de Admin - Gerenciamento de Dados

## Descrição
Estes endpoints permitem popular e gerenciar dados de teste no banco de dados de forma rápida e fácil.

## Endpoints Disponíveis

### 1. Popular Produtos (POST)
**Endpoint:** `POST http://localhost:8080/admin/dados/popular-produtos`

**Descrição:** Insere 10 produtos de teste no banco de dados

**Resposta Sucesso (200):**
```json
{
  "mensagem": "✅ 10 produtos inseridos com sucesso!",
  "sucesso": true,
  "dados": [
    {
      "id": 1,
      "descricao": "Pizza Calabresa",
      "preco": 5500
    },
    {
      "id": 2,
      "descricao": "Pizza Queijo e Presunto",
      "preco": 6000
    },
    ...
  ]
}
```

**Produtos Inseridos:**
- Pizza Calabresa (R$ 55,00)
- Pizza Queijo e Presunto (R$ 60,00)
- Pizza Margherita (R$ 40,00)
- Pizza Frango com Catupiry (R$ 65,00)
- Pizza Portuguesa (R$ 70,00)
- Pizza 4 Queijos (R$ 65,00)
- Pizza Vegetariana (R$ 50,00)
- Refrigerante 2L (R$ 8,00)
- Suco Natural (R$ 6,00)
- Cerveja Premium (R$ 12,00)

---

### 2. Verificar Total de Produtos (GET)
**Endpoint:** `GET http://localhost:8080/admin/dados/total-produtos`

**Descrição:** Retorna a quantidade total de produtos no banco

**Resposta Sucesso (200):**
```json
{
  "mensagem": "Total de produtos: 10",
  "sucesso": true,
  "dados": 10
}
```

---

### 3. Limpar Todos os Produtos (DELETE)
**Endpoint:** `DELETE http://localhost:8080/admin/dados/limpar-produtos`

**Descrição:** Remove todos os produtos do banco de dados

**Resposta Sucesso (200):**
```json
{
  "mensagem": "✅ Todos os produtos foram removidos!",
  "sucesso": true,
  "dados": null
}
```

---

## 📝 Fluxo Recomendado de Testes

1. **Limpar dados antigos** (opcional):
   ```
   DELETE http://localhost:8080/admin/dados/limpar-produtos
   ```

2. **Popular novo dados**:
   ```
   POST http://localhost:8080/admin/dados/popular-produtos
   ```

3. **Verificar quantidade**:
   ```
   GET http://localhost:8080/admin/dados/total-produtos
   ```

4. **Testar criação de pedido**:
   ```
   POST http://localhost:8080/pedidos/criar
   ```

---

## 🚀 Exemplo de Uso com HTTP Client

Abra o arquivo `auth-requests.http` e clique em "Run" para executar os endpoints:

```http
### Admin: Popular produtos no banco
POST http://localhost:8080/admin/dados/popular-produtos

### Admin: Total de produtos
GET http://localhost:8080/admin/dados/total-produtos

### Admin: Limpar todos os produtos
DELETE http://localhost:8080/admin/dados/limpar-produtos
```

---

## ⚠️ Notas Importantes

- ⚡ Os IDs dos produtos começam em 1 e vão até 10
- 💰 Os preços estão em centavos (5500 = R$ 55,00)
- 🔄 Cada vez que você chama "Popular Produtos", os dados antigos são mantidos
- 🗑️ Use "Limpar" antes de "Popular" para limpar dados antigos
- ✅ A resposta sempre retorna um objeto com: `mensagem`, `sucesso` e `dados`
