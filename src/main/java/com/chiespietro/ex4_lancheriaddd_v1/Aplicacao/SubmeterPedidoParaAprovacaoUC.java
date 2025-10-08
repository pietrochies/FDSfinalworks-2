package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.EstoqueService;
import java.util.List;

public class SubmeterPedidoParaAprovacaoUC {

    private PedidoService pedidoService;
    private EstoqueService estoqueService;

    public SubmeterPedidoParaAprovacaoUC(PedidoService pedidoService, EstoqueService estoqueService) {
        this.pedidoService = pedidoService;
        this.estoqueService = estoqueService;
    }

    public ResultadoSubmissaoPedido executar(Cliente cliente, List<ItemPedido> itens, int pedidosUltimos20Dias) {
        
        if (!estoqueService.temEstoqueSuficiente(itens)) {
            return ResultadoSubmissaoPedido.negadoPorFaltaDeIngredientes();
        }
        // 2. Criar o pedido com status APROVADO
        Pedido pedido = new Pedido(0, cliente, null, itens, Pedido.Status.APROVADO, 0, 0, 0, 0);
        double precoFinal = pedidoService.calcularCustoFinal(pedido, pedidosUltimos20Dias);
        // 3. Atualizar valores do pedido
        // (Você pode criar setters ou um método para atualizar esses campos)
        // 4. Retornar resultado aprovado
        return ResultadoSubmissaoPedido.aprovado(pedido, precoFinal);
    }

    public static class ResultadoSubmissaoPedido {
        public final boolean aprovado;
        public final Pedido pedido;
        public final double precoFinal;
        public final String motivoNegacao;

        private ResultadoSubmissaoPedido(boolean aprovado, Pedido pedido, double precoFinal, String motivoNegacao) {
            this.aprovado = aprovado;
            this.pedido = pedido;
            this.precoFinal = precoFinal;
            this.motivoNegacao = motivoNegacao;
        }
        public static ResultadoSubmissaoPedido aprovado(Pedido pedido, double precoFinal) {
            return new ResultadoSubmissaoPedido(true, pedido, precoFinal, null);
        }
        public static ResultadoSubmissaoPedido negadoPorFaltaDeIngredientes() {
            return new ResultadoSubmissaoPedido(false, null, 0, "Falta de ingredientes");
        }
    }
}
