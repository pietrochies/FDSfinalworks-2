package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_estoque")
public class ItemEstoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;
    
    @Column(name = "quantidade")
    private int quantidade;

    public ItemEstoque(Ingrediente ingrediente, int quantidade) {
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    public Ingrediente getIngrediente() { return ingrediente; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}
