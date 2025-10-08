package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public class PedidoService {
    /**
     * Calcula o custo final do pedido, aplicando desconto e impostos conforme regras de negócio.
     * @param pedido O pedido a ser calculado
     * @param pedidosUltimos20Dias Quantidade de pedidos do cliente nos últimos 20 dias
     * @return custo final do pedido
     */
    public double calcularCustoFinal(Pedido pedido, int pedidosUltimos20Dias) {
        double somaItens = 0.0;
        double desconto = 0.0;
        boolean aplicaDesconto = pedidosUltimos20Dias > 3;
        for (ItemPedido itemPedido : pedido.getItens()) {
            double precoItem = itemPedido.getItem().getPreco() / 100.0; 
            double precoComDesconto = precoItem;
            if (aplicaDesconto) {
                precoComDesconto = precoItem * 0.93; // 7% de desconto
                desconto += (precoItem - precoComDesconto) * itemPedido.getQuantidade();
            }
            somaItens += precoComDesconto * itemPedido.getQuantidade();
        }
        double impostos = somaItens * 0.10; // 10% de imposto
        return somaItens + impostos;
    }
}
