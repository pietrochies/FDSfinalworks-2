package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository {
    List<Pedido> findByStatusAndDataEntregaBetween(Pedido.Status status, LocalDateTime inicio, LocalDateTime fim);
    Pedido findById(Long id);
    Pedido save(Pedido pedido); // Alterado para retornar Pedido
    void delete(Pedido pedido);
}