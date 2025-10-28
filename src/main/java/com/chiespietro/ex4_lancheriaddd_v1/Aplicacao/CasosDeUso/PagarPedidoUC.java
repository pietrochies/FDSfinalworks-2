package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.CasosDeUso;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.CozinhaService;

public class PagarPedidoUC {

    private final PedidoRepository pedidoRepository;
    private final CozinhaService cozinhaService;

    public PagarPedidoUC(PedidoRepository pedidoRepository, CozinhaService cozinhaService) {
        this.pedidoRepository = pedidoRepository;
        this.cozinhaService = cozinhaService;
    }

    public void executar(Long pedidoId) {
        // 1. Recupera o pedido
        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        
        pedido.setStatus(Pedido.Status.PAGO);
        System.out.println("Pedido pago: " + pedido);

        
        pedidoRepository.save(pedido);// Atualiza o pedido no repositório

        
        cozinhaService.chegadaDePedido(pedido); // Notifica a cozinha
    }
}
