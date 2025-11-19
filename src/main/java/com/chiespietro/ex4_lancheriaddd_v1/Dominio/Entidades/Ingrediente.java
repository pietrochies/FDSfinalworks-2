package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    private long id;
    
    @Column(name = "descricao")
    private String descricao;

    @ManyToMany(mappedBy = "ingredientes")
    private List<Receita> receitas;

    public Ingrediente(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public long getId() { return id; }
    public String getDescricao() { return descricao; }
    public List<Receita> getReceitas() { return receitas; }
}
