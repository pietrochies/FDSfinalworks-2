# Sistema de Descontos - Documentação

## Visão Geral

O sistema de descontos foi implementado com dois tipos de descontos automáticos baseados no perfil do cliente:

### 1. **ClienteFrequente** (7% de desconto)
- **Critério**: Cliente com mais de 3 pedidos nos últimos 20 dias
- **Desconto**: 7%
- **Aplicação**: Automática ao processar pagamento

### 2. **ClienteGastador** (15% de desconto)
- **Critério**: Cliente que gastou mais de R$ 500,00 nos últimos 30 dias
- **Desconto**: 15%
- **Aplicação**: Automática ao processar pagamento

## Arquitetura

### Entidades

#### **Desconto** (`Dominio/Entidades/Desconto.java`)
```java
- id: Long (chave primária)
- codigo: String (único, ex: "ClienteFrequente")
- descricao: String
- percentual: Double
- ativo: Boolean
```

#### **Pedido** (atualizada)
```java
- descontoAplicado: Desconto (nova relação ManyToOne)
- desconto: double (valor total do desconto aplicado)
- impostos: double
- valorCobrado: double (valor final com desconto e impostos)
```

### Repositórios

1. **DescontoRepository** (Interface)
   - `findByCodigo(String codigo)`
   - `findById(Long id)`
   - `save(Desconto desconto)`

2. **DescontoRepositoryJDBC** (Implementação JPA)
   - Implementa DescontoRepository
   - Estende JpaRepository

3. **PedidoRepositoryJDBC** (Atualizada)
   - Novo método: `findByClienteAndDataHoraPagamentoBetween()`
   - Permite buscar pedidos de um cliente em um intervalo de datas

### Serviços

#### **DescontoService**
Responsável pela lógica de negócio dos descontos:

```java
- obterDescontoAplicavel(Cliente cliente): Desconto
  └─ Verifica ClienteFrequente (últimos 20 dias)
  └─ Verifica ClienteGastador (últimos 30 dias)
  └─ Retorna o desconto aplicável ou null

- verificarClienteFrequente(Cliente cliente): boolean
  └─ Conta pedidos com status != NOVO e != CANCELADO
  └─ Retorna true se > 3 pedidos nos últimos 20 dias

- verificarClienteGastador(Cliente cliente): boolean
  └─ Soma valores de pedidos com status != NOVO e != CANCELADO
  └─ Retorna true se total > R$ 500 nos últimos 30 dias

- calcularValorDesconto(Cliente cliente, double valorPedido): double
```

#### **PedidoService** (atualizado)
```java
- calcularCustoFinal(Pedido pedido): double
  └─ Calcula valor total dos itens
  └─ Aplica desconto automático via DescontoService
  └─ Calcula impostos (10%) sobre valor com desconto
  └─ Retorna valor final a cobrar
```

### Use Cases

#### **PagarPedidoUC** (atualizado)
1. Recupera o pedido
2. **Calcula custo final com desconto** ← NOVO
3. Define status como PAGO
4. Atualiza no repositório
5. Notifica cozinha

#### **AplicarDescontoUC** (novo)
- UseCase separado para aplicar desconto manualmente
- Útil para operações de teste ou ajustes

## Banco de Dados

### Nova Tabela: `descontos`
```sql
CREATE TABLE descontos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  codigo VARCHAR(100) NOT NULL UNIQUE,
  descricao VARCHAR(255),
  percentual DOUBLE NOT NULL,
  ativo BOOLEAN NOT NULL DEFAULT TRUE
);
```

### Tabela Atualizada: `pedidos`
```sql
ALTER TABLE pedidos ADD COLUMN desconto_id BIGINT;
ALTER TABLE pedidos ADD FOREIGN KEY (desconto_id) REFERENCES descontos(id);
```

### Dados Iniciais
```sql
INSERT INTO descontos (codigo, descricao, percentual, ativo) 
VALUES ('ClienteFrequente', 'Desconto para cliente frequente (mais de 3 pedidos em 20 dias)', 7.0, true);

INSERT INTO descontos (codigo, descricao, percentual, ativo) 
VALUES ('ClienteGastador', 'Desconto para cliente que gastou mais de R$ 500 em 30 dias', 15.0, true);
```

## Fluxo de Operação

```
Cliente solicita pagamento do pedido
    ↓
PagarPedidoUC.executar(pedidoId)
    ↓
PedidoService.calcularCustoFinal(pedido)
    ↓
DescontoService.obterDescontoAplicavel(cliente)
    ├─ Busca pedidos cliente nos últimos 20 dias
    └─ Se > 3 pedidos → retorna "ClienteFrequente" (7%)
    ├─ Busca pedidos cliente nos últimos 30 dias
    └─ Se gasto > R$ 500 → retorna "ClienteGastador" (15%)
    ├─ Caso contrário → retorna null
    ↓
Aplica desconto, calcula impostos
    ↓
Retorna valor final
    ↓
Pedido atualizado no repositório
    ↓
Cozinha notificada
```

## Exemplo de Cálculo

**Pedido de R$ 100,00**

### Cenário 1: Cliente Frequente (7% de desconto)
```
Valor bruto: R$ 100,00
Desconto (7%): R$ 7,00
Valor com desconto: R$ 93,00
Impostos (10%): R$ 9,30
Total a cobrar: R$ 102,30
```

### Cenário 2: Cliente Gastador (15% de desconto)
```
Valor bruto: R$ 100,00
Desconto (15%): R$ 15,00
Valor com desconto: R$ 85,00
Impostos (10%): R$ 8,50
Total a cobrar: R$ 93,50
```

### Cenário 3: Sem desconto
```
Valor bruto: R$ 100,00
Desconto: R$ 0,00
Valor com desconto: R$ 100,00
Impostos (10%): R$ 10,00
Total a cobrar: R$ 110,00
```

## Notas Importantes

1. **Prioridade**: ClienteFrequente é verificado antes de ClienteGastador
2. **Pedidos Cancelados**: Não são considerados no cálculo dos descontos
3. **Pedidos Novos**: Não são considerados como "pagos" no cálculo
4. **Impostos**: Aplicados APÓS o desconto
5. **Histórico**: Os descontos aplicados são registrados na coluna `desconto_id` da tabela pedidos

## Testes Recomendados

1. Criar cliente e fazer 4 pedidos em 20 dias → Verificar aplicação de 7%
2. Criar cliente e fazer pedidos totalizando > R$ 500 em 30 dias → Verificar aplicação de 15%
3. Criar cliente novo → Verificar sem desconto
4. Cancelar pedidos → Verificar se não contam para desconto
