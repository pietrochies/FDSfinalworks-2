package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "cardapios")
public class Cardapio {
    @Embedded
    private CabecalhoCardapio cabecalhoCardapio;
    
    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL)
    private List<Produto> produtos;

    public Cardapio(long id, String titulo, List<Produto> produtos) {
        this.cabecalhoCardapio = new CabecalhoCardapio(id,titulo);
        this.produtos = produtos;
    }

    public long getId() { return cabecalhoCardapio.id(); }
    public String getTitulo(){ return cabecalhoCardapio.titulo(); }
    public CabecalhoCardapio getCabecalhoCardapio(){ return cabecalhoCardapio; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos){this.produtos = produtos;}
}
