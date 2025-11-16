package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Enumerated(EnumType.STRING)
    public enum Status {
        NOVO,
        APROVADO,
        PAGO,
        AGUARDANDO,
        PREPARACAO,
        PRONTO,
        TRANSPORTE,
        ENTREGUE,
        CANCELADO
    }
    
    @Id
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_cpf")
    private Cliente cliente;
    
    @Column(name = "data_hora_pagamento")
    private LocalDateTime dataHoraPagamento;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> itens;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    
    @Column(name = "valor")
    private double valor;
    
    @Column(name = "impostos")
    private double impostos;
    
    @Column(name = "desconto")
    private double desconto;
    
    @Column(name = "valor_cobrado")
    private double valorCobrado;

    public Pedido(long id, Cliente cliente, LocalDateTime dataHoraPagamento, List<ItemPedido> itens,
            Pedido.Status status, double valor, double impostos, double desconto, double valorCobrado) {
        this.id = id;
        this.cliente = cliente;
        this.dataHoraPagamento = dataHoraPagamento;
        this.itens = itens;
        this.status = status;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
    }

    public long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public double getValor() {
        return valor;
    }

    public double getImpostos() {
        return impostos;
    }

    public double getDesconto() {
        return desconto;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }
}
