package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.CasosDeUso;

import java.time.LocalDateTime;
import java.util.List;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;

public class ListarPedidosEntreguesEntreDatasUC {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosEntreguesEntreDatasUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> executar(LocalDateTime inicio, LocalDateTime fim) { // depois mudar para LocalDate
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias."); 
        }

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial."); // depois mudar para LocalDate
        }

        return pedidoRepository.findByStatusAndDataEntregaBetween(
            Pedido.Status.ENTREGUE,
            inicio,
            fim
        );
    }
}
