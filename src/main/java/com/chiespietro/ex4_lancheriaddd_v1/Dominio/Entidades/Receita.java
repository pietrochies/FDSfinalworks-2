package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "receitas")
public class Receita {
    @Id
    private long id;
    
    @Column(name = "titulo")
    private String titulo;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "receita_ingredientes",
        joinColumns = @JoinColumn(name = "receita_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<Ingrediente> ingredientes;

    public Receita(long id, String titulo, List<Ingrediente> ingredientes) {
        this.id = id;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
    }

    public long getId() { return id; }
    public String getTitulo(){ return titulo; }
    public List<Ingrediente> getIngredientes() { return ingredientes; }
}
