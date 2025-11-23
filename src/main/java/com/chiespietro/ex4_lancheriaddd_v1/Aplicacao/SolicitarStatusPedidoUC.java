package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class SolicitarStatusPedidoUC {
    

    public StatusPedidoResponse executar(Pedido pedido) {
        return new StatusPedidoResponse(pedido.getId(), pedido.getStatus().name());
    }

    public static class StatusPedidoResponse {
        public final long pedidoId;
        public final String status;

        public StatusPedidoResponse(long pedidoId, String status) {
            this.pedidoId = pedidoId;
            this.status = status;
        }
    }
}
