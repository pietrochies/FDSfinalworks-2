package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs;

import java.util.List;

public class CriarPedidoRequest {
    private String clienteCpf;
    private List<ItemPedidoDTO> itens;

    public CriarPedidoRequest() {}

    public CriarPedidoRequest(String clienteCpf, List<ItemPedidoDTO> itens) {
        this.clienteCpf = clienteCpf;
        this.itens = itens;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public static class ItemPedidoDTO {
        private Long produtoId;
        private int quantidade;

        public ItemPedidoDTO() {}

        public ItemPedidoDTO(Long produtoId, int quantidade) {
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
}
