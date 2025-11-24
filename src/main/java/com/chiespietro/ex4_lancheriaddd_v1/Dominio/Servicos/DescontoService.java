package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.DescontoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;

@Service
public class DescontoService {

    private final DescontoRepository descontoRepository;
    private final PedidoRepository pedidoRepository;

    public DescontoService(DescontoRepository descontoRepository, PedidoRepository pedidoRepository) {
        this.descontoRepository = descontoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Determina o desconto aplicável para um cliente
     * 
     * @param cliente O cliente
     * @return O desconto aplicável, ou null se nenhum desconto se aplica
     */
    public Desconto obterDescontoAplicavel(Cliente cliente) {
        // Verifica ClienteFrequente: pedidos nos últimos 20 dias
        if (verificarClienteFrequente(cliente)) {
            return descontoRepository.findByCodigo("ClienteFrequente");
        }

        // Verifica ClienteGastador: gasto > R$ 500 nos últimos 30 dias
        if (verificarClienteGastador(cliente)) {
            return descontoRepository.findByCodigo("ClienteGastador");
        }

        return null;
    }

    /**
     * Verifica se o cliente é frequente (mais de 3 pedidos nos últimos 20 dias)
     */
    private boolean verificarClienteFrequente(Cliente cliente) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime vinte_dias_atras = agora.minusDays(20);

        List<Pedido> pedidos = pedidoRepository.findByClienteAndDataHoraPagamentoBetween(cliente, vinte_dias_atras, agora);
        
        // Conta apenas pedidos com status PAGO ou posterior
        long pedidosPagos = pedidos.stream()
            .filter(p -> p.getStatus() != Pedido.Status.NOVO && p.getStatus() != Pedido.Status.CANCELADO)
            .count();

        return pedidosPagos > 3;
    }

    /**
     * Verifica se o cliente é gastador (gasto > R$ 500 nos últimos 30 dias)
     */
    private boolean verificarClienteGastador(Cliente cliente) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trinta_dias_atras = agora.minusDays(30);

        List<Pedido> pedidos = pedidoRepository.findByClienteAndDataHoraPagamentoBetween(cliente, trinta_dias_atras, agora);

        // Soma o valor de todos os pedidos PAGO ou posteriores
        double valorTotal = pedidos.stream()
            .filter(p -> p.getStatus() != Pedido.Status.NOVO && p.getStatus() != Pedido.Status.CANCELADO)
            .mapToDouble(Pedido::getValor)
            .sum();

        return valorTotal > 500.0;
    }

    /**
     * Calcula o valor do desconto aplicável
     */
    public double calcularValorDesconto(Cliente cliente, double valorPedido) {
        Desconto desconto = obterDescontoAplicavel(cliente);
        if (desconto == null) {
            return 0.0;
        }
        return desconto.calcularValorDesconto(valorPedido);
    }
}
