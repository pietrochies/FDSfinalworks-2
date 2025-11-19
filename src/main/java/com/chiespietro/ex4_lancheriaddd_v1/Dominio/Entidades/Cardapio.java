package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "cardapios")
public class Cardapio {
    @Id
    private long id;
    
    @Column(name = "titulo")
    private String titulo;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cardapio_produto",
        joinColumns = @JoinColumn(name = "cardapio_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;

    public Cardapio() {}

    public Cardapio(long id, String titulo, List<Produto> produtos) {
        this.id = id;
        this.titulo = titulo;
        this.produtos = produtos;
    }

    public long getId() { return id; }
    public String getTitulo() { return titulo; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}

