package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.SolicitarStatusPedidoUC;
import com.chiespietro.ex4_lancheriaddd_v1.Aplicacao.CriarPedidoUC;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs.CriarPedidoRequest;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin("*")
public class PedidosController {

    private PedidoRepository pedidoRepository;
    private PagarPedidoUC pagarPedidoUC;
    private CancelarPedidoUC cancelarPedidoUC;
    private SolicitarStatusPedidoUC solicitarStatusPedidoUC;
    private CriarPedidoUC criarPedidoUC;

    @Autowired
    public PedidosController(
            PedidoRepository pedidoRepository,
            PagarPedidoUC pagarPedidoUC,
            CancelarPedidoUC cancelarPedidoUC,
            SolicitarStatusPedidoUC solicitarStatusPedidoUC,
            CriarPedidoUC criarPedidoUC) {
        this.pedidoRepository = pedidoRepository;
        this.pagarPedidoUC = pagarPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.solicitarStatusPedidoUC = solicitarStatusPedidoUC;
        this.criarPedidoUC = criarPedidoUC;
    }

    /**
     * GET /pedidos/{id} - Recuperar pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarPedido(@PathVariable Long id, HttpSession session) {
        try {
            // Verifica autenticação
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"erro\": \"Usuário não autenticado\"}");
            }

            Pedido pedido = pedidoRepository.findById(id);
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"erro\": \"Pedido não encontrado\"}");
            }

            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * POST /pedidos/criar - Criar novo pedido (UC - CriarPedidoUC)
     */
    @PostMapping("/criar")
    public ResponseEntity<?> criarPedido(@RequestBody CriarPedidoRequest request, HttpSession session) {
        try {
            // Verifica autenticação
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"erro\": \"Usuário não autenticado\"}");
            }

            // Cria objeto Cliente a partir do CPF (pode ser expandido para recuperar dados completos)
            Cliente cliente = new Cliente(
                request.getClienteCpf(),
                "", // nome será preenchido posteriormente
                "", // celular
                "", // endereço
                ""  // email
            );

            // Converte os DTOs para o formato esperado pelo UC
            java.util.List<CriarPedidoUC.ItemPedidoRequest> itens = new java.util.ArrayList<>();
            for (CriarPedidoRequest.ItemPedidoDTO item : request.getItens()) {
                itens.add(new CriarPedidoUC.ItemPedidoRequest(item.getProdutoId(), item.getQuantidade()));
            }

            // Executa o UC
            Pedido pedidoCriado = criarPedidoUC.executar(cliente, itens);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CriarPedidoUC.CriarPedidoResponse(pedidoCriado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * POST /pedidos/{id}/pagar - Pagar pedido (UC - PagarPedidoUC)
     */
    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagarPedido(@PathVariable Long id, HttpSession session) {
        try {
            // Verifica autenticação
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"erro\": \"Usuário não autenticado\"}");
            }

            Pedido pedido = pedidoRepository.findById(id);
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"erro\": \"Pedido não encontrado\"}");
            }

            pagarPedidoUC.executar(id);
            Pedido pedidoAtualizado = pedidoRepository.findById(id);
            
            return ResponseEntity.ok(new PedidoPagoResponse(
                pedidoAtualizado.getId(),
                pedidoAtualizado.getStatus().name(),
                "Pedido pago com sucesso"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * GET /pedidos/{id}/status - Solicitar status do pedido (UC - SolicitarStatusPedidoUC)
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<?> solicitarStatusPedido(@PathVariable Long id, HttpSession session) {
        try {
            // Verifica autenticação
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"erro\": \"Usuário não autenticado\"}");
            }

            Pedido pedido = pedidoRepository.findById(id);
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"erro\": \"Pedido não encontrado\"}");
            }

            SolicitarStatusPedidoUC.StatusPedidoResponse response = solicitarStatusPedidoUC.executar(pedido);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * POST /pedidos/{id}/cancelar - Cancelar pedido (UC - CancelarPedidoUC)
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id, HttpSession session) {
        try {
            // Verifica autenticação
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"erro\": \"Usuário não autenticado\"}");
            }

            Pedido pedido = pedidoRepository.findById(id);
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"erro\": \"Pedido não encontrado\"}");
            }

            CancelarPedidoUC.ResultadoCancelamentoPedido resultado = cancelarPedidoUC.executar(pedido);
            
            if (resultado.cancelado) {
                pedidoRepository.save(resultado.pedido);
                return ResponseEntity.ok(new PedidoCanceladoResponse(
                    resultado.pedido.getId(),
                    resultado.pedido.getStatus().name(),
                    "Pedido cancelado com sucesso"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErroResponse(resultado.motivo));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    // DTOs de Resposta
    public static class PedidoPagoResponse {
        public long pedidoId;
        public String status;
        public String mensagem;

        public PedidoPagoResponse(long pedidoId, String status, String mensagem) {
            this.pedidoId = pedidoId;
            this.status = status;
            this.mensagem = mensagem;
        }
    }

    public static class PedidoCanceladoResponse {
        public long pedidoId;
        public String status;
        public String mensagem;

        public PedidoCanceladoResponse(long pedidoId, String status, String mensagem) {
            this.pedidoId = pedidoId;
            this.status = status;
            this.mensagem = mensagem;
        }
    }

    public static class ErroResponse {
        public String erro;

        public ErroResponse(String erro) {
            this.erro = erro;
        }
    }
}
