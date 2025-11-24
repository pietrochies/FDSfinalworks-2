package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;

@Component
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoRepositoryJDBC pedidoRepositoryJDBC;

    @Autowired
    public PedidoRepositoryAdapter(PedidoRepositoryJDBC pedidoRepositoryJDBC) {
        this.pedidoRepositoryJDBC = pedidoRepositoryJDBC;
    }

    @Override
    public Pedido findById(Long id) {
        if (id == null) {
            return null;
        }
        return pedidoRepositoryJDBC.findById(id).orElse(null);
    }

    @Override
    public void save(Pedido pedido) {
        if (pedido != null) {
            pedidoRepositoryJDBC.save(pedido);
        }
    }

    @Override
    public List<Pedido> findByStatusAndDataEntregaBetween(Pedido.Status status, LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepositoryJDBC.findByStatusAndDataEntregaBetween(status, inicio, fim);
    }

    @Override
    public List<Pedido> findByClienteAndDataHoraPagamentoBetween(Cliente cliente, LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepositoryJDBC.findByClienteAndDataHoraPagamentoBetween(cliente, inicio, fim);
    }
}
