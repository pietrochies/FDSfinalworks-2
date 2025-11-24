package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.CozinhaService;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {

    private final PedidoRepository pedidoRepository;
    private final CozinhaService cozinhaService;
    private final PedidoService pedidoService;

    @Autowired
    public PagarPedidoUC(PedidoRepository pedidoRepository, CozinhaService cozinhaService, PedidoService pedidoService) {
        this.pedidoRepository = pedidoRepository;
        this.cozinhaService = cozinhaService;
        this.pedidoService = pedidoService;
    }

    public void executar(Long pedidoId) {
        // 1. Recupera o pedido
        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        // 2. Calcula o custo final com desconto aplicável
        pedidoService.calcularCustoFinal(pedido);
        
        // 3. Define o status como PAGO
        pedido.setStatus(Pedido.Status.PAGO);
        System.out.println("Pedido pago: " + pedido);

        // 4. Atualiza o pedido no repositório
        pedidoRepository.save(pedido);

        // 5. Notifica a cozinha
        cozinhaService.chegadaDePedido(pedido);
    }
}
