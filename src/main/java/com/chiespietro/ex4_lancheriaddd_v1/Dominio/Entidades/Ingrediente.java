package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    private long id;
    
    @Column(name = "descricao")
    private String descricao;

    public Ingrediente(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public long getId() { return id; }
    public String getDescricao() { return descricao; }
}
