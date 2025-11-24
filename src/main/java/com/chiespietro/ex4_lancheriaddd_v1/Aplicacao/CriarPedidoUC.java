package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.PedidoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;

@Component
public class CriarPedidoUC {

    private final PedidoRepository pedidoRepository;
    private final ProdutosRepository produtosRepository;

    @Autowired
    public CriarPedidoUC(
            PedidoRepository pedidoRepository,
            ProdutosRepository produtosRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtosRepository = produtosRepository;
    }

    public Pedido executar(Cliente cliente, List<ItemPedidoRequest> itensRequest) {
        // 1. Valida o cliente
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }

        // 2. Valida e recupera os produtos, calculando o valor total
        List<ItemPedido> itens = new ArrayList<>();
        double valorTotal = 0;

        for (ItemPedidoRequest item : itensRequest) {
            Produto produto = produtosRepository.recuperaProdutoPorid(item.getProdutoId());
            if (produto == null) {
                throw new IllegalArgumentException("Produto não encontrado: " + item.getProdutoId());
            }

            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }

            ItemPedido itemPedido = new ItemPedido(produto, item.getQuantidade());
            itens.add(itemPedido);
            valorTotal += produto.getPreco() * item.getQuantidade();
        }

        if (itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter pelo menos um item");
        }

        // 3. Calcula impostos (10% do valor)
        double impostos = valorTotal * 0.10;

        // 4. Cria o pedido com status NOVO
        Pedido pedido = new Pedido(
            gerarIdPedido(),
            cliente,
            null, // Data de pagamento será preenchida quando pago
            itens,
            Pedido.Status.NOVO,
            valorTotal,
            impostos,
            0, // Desconto inicial é 0
            valorTotal + impostos // Valor cobrado inicial sem desconto
        );

        // 5. Persiste o pedido
        pedidoRepository.save(pedido);

        System.out.println("Pedido criado com sucesso: " + pedido.getId());
        return pedido;
    }

    // Gera um ID único para o pedido (timestamp + random)
    private long gerarIdPedido() {
        return System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    // DTO interno para receber itens do pedido
    public static class ItemPedidoRequest {
        private Long produtoId;
        private int quantidade;

        public ItemPedidoRequest() {}

        public ItemPedidoRequest(Long produtoId, int quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }

        public Long getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }

    // DTO de resposta
    public static class CriarPedidoResponse {
        public long pedidoId;
        public String clienteCpf;
        public String status;
        public double valor;
        public double impostos;
        public double valorCobrado;
        public String mensagem;

        public CriarPedidoResponse(Pedido pedido) {
            this.pedidoId = pedido.getId();
            this.clienteCpf = pedido.getCliente().getCpf();
            this.status = pedido.getStatus().name();
            this.valor = pedido.getValor();
            this.impostos = pedido.getImpostos();
            this.valorCobrado = pedido.getValorCobrado();
            this.mensagem = "Pedido criado com sucesso";
        }
    }
}
