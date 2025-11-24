package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios;

import java.time.LocalDateTime;
import java.util.List;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface PedidoRepository {
    // Recupera um pedido pelo id
    Pedido findById(Long id);

    // Salva/atualiza um pedido
    void save(Pedido pedido);

    // Lista pedidos por status e intervalo de data de entrega
    List<Pedido> findByStatusAndDataEntregaBetween(Pedido.Status status, LocalDateTime inicio, LocalDateTime fim);
    
    // Lista pedidos de um cliente entre datas
    List<Pedido> findByClienteAndDataHoraPagamentoBetween(Cliente cliente, LocalDateTime inicio, LocalDateTime fim);
}
