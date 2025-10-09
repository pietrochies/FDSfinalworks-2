package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class CancelarPedidoUC {
    // Em um cenário real, você buscaria o pedido por ID e validaria o cliente
    
    public ResultadoCancelamentoPedido executar(Pedido pedido) {
        if (pedido.getStatus() == Pedido.Status.APROVADO) {
            // Só pode cancelar se não estiver pago
            pedido.setStatus(Pedido.Status.CANCELADO);
            return ResultadoCancelamentoPedido.cancelado(pedido);
        } else {
            return ResultadoCancelamentoPedido.naoCancelado("Pedido não está aprovado ou já foi pago.");
        }
    }

    public static class ResultadoCancelamentoPedido {
        public final boolean cancelado;
        public final Pedido pedido;
        public final String motivo;

        private ResultadoCancelamentoPedido(boolean cancelado, Pedido pedido, String motivo) {
            this.cancelado = cancelado;
            this.pedido = pedido;
            this.motivo = motivo;
        }
        public static ResultadoCancelamentoPedido cancelado(Pedido pedido) {
            return new ResultadoCancelamentoPedido(true, pedido, null);
        }
        public static ResultadoCancelamentoPedido naoCancelado(String motivo) {
            return new ResultadoCancelamentoPedido(false, null, motivo);
        }
    }
}
