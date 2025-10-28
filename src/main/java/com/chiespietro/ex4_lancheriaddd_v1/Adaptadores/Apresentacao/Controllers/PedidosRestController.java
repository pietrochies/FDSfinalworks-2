package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Controllers;

import com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.CasosDeUso.*;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosRestController {

    private final SubmeterPedidoParaAprovacaoUC submeterPedidoUC;
    private final CancelarPedidoUC cancelarPedidoUC;
    private final SolicitarStatusPedidoUC solicitarStatusPedidoUC;
    private final PagarPedidoUC pagarPedidoUC;
    private final ListarPedidosEntreguesEntreDatasUC listarPedidosUC;

    public PedidosRestController(SubmeterPedidoParaAprovacaoUC submeterPedidoUC,
                                CancelarPedidoUC cancelarPedidoUC,
                                SolicitarStatusPedidoUC solicitarStatusPedidoUC,
                                PagarPedidoUC pagarPedidoUC,
                                ListarPedidosEntreguesEntreDatasUC listarPedidosUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.solicitarStatusPedidoUC = solicitarStatusPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.listarPedidosUC = listarPedidosUC;
    }

    // POST /api/pedidos
    @PostMapping
    public Pedido criarPedido(@RequestBody Pedido pedido) {
        return submeterPedidoUC.executar(pedido);
    }

    // PUT /api/pedidos/{id}/cancelar
    @PutMapping("/{id}/cancelar")
    public void cancelarPedido(@PathVariable int id) {
        cancelarPedidoUC.executar(id);
    }

    // GET /api/pedidos/{id}/status
    @GetMapping("/{id}/status")
    public String consultarStatus(@PathVariable int id) {
        return solicitarStatusPedidoUC.executar(id);
    }

    // PUT /api/pedidos/{id}/pagar
    @PutMapping("/{id}/pagar")
    public void pagarPedido(@PathVariable int id) {
        pagarPedidoUC.executar(id);
    }

    // GET /api/pedidos/entregues?inicio=2025-10-01&fim=2025-10-31
    @GetMapping("/entregues")
    public List<Pedido> listarPedidosEntregues(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return listarPedidosUC.executar(inicio, fim);
    }
}
