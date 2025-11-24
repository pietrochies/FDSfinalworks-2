package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoService;

@Component
public class AplicarDescontoUC {

    private final PedidoRepository pedidoRepository;
    private final DescontoService descontoService;

    @Autowired
    public AplicarDescontoUC(PedidoRepository pedidoRepository, DescontoService descontoService) {
        this.pedidoRepository = pedidoRepository;
        this.descontoService = descontoService;
    }

    /**
     * Aplica o desconto automático baseado no perfil do cliente
     */
    public void executar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoId);
        }

        // Obtém o desconto aplicável
        Desconto descontoAplicavel = descontoService.obterDescontoAplicavel(pedido.getCliente());

        if (descontoAplicavel != null) {
            pedido.setDescontoAplicado(descontoAplicavel);
            double valorDesconto = descontoAplicavel.calcularValorDesconto(pedido.getValor());
            pedido.setDesconto(valorDesconto);
            
            System.out.println("Desconto aplicado: " + descontoAplicavel.getCodigo() + 
                             " (" + descontoAplicavel.getPercentual() + "%) - Valor: R$ " + 
                             String.format("%.2f", valorDesconto));
        } else {
            System.out.println("Nenhum desconto aplicável para este cliente");
        }

        pedidoRepository.save(pedido);
    }
}
