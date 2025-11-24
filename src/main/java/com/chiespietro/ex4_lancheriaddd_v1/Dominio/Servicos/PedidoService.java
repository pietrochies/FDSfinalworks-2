package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@Service
public class PedidoService {
    
    private final DescontoService descontoService;

    public PedidoService(DescontoService descontoService) {
        this.descontoService = descontoService;
    }

    /**
     * Calcula o custo final do pedido, aplicando desconto e impostos conforme regras de negócio.
     * Descontos aplicáveis:
     * - ClienteFrequente: Cliente com mais de 3 pedidos nos últimos 20 dias (7% de desconto)
     * - ClienteGastador: Cliente que gastou mais de R$ 500 nos últimos 30 dias (15% de desconto)
     * 
     * @param pedido O pedido a ser calculado
     * @return custo final do pedido
     */
    public double calcularCustoFinal(Pedido pedido) {
        double somaItens = 0.0;

        // Calcula o valor total dos itens
        for (ItemPedido itemPedido : pedido.getItens()) {
            double precoItem = itemPedido.getItem().getPreco() / 100.0;
            somaItens += precoItem * itemPedido.getQuantidade();
        }

        // Obtém o desconto aplicável
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(pedido.getCliente());
        double valorDesconto = 0.0;

        if (descontoAplicavel != null) {
            valorDesconto = descontoAplicavel.calcularValorDesconto(somaItens);
            pedido.setDescontoAplicado(descontoAplicavel);
        }

        double valorComDesconto = somaItens - valorDesconto;
        double impostos = valorComDesconto * 0.10; // 10% de imposto sobre o valor com desconto

        pedido.setDesconto(valorDesconto);
        pedido.setImpostos(impostos);
        pedido.setValorCobrado(valorComDesconto + impostos);

        return pedido.getValorCobrado();
    }

    /**
     * Método legado mantido para compatibilidade
     * @deprecated Use calcularCustoFinal(Pedido pedido) ao invés
     */
    @Deprecated
    public double calcularCustoFinal(Pedido pedido, int pedidosUltimos20Dias) {
        return calcularCustoFinal(pedido);
    }
}
