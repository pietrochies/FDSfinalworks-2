package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "descontos")
public class Desconto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", unique = true, nullable = false)
    private String codigo;
    
    @Column(name = "descricao")
    private String descricao;
    
    @Column(name = "percentual")
    private Double percentual;
    
    @Column(name = "ativo")
    private Boolean ativo;

    public Desconto() {}

    public Desconto(String codigo, String descricao, Double percentual) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.percentual = percentual;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getPercentual() {
        return percentual;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Double calcularValorDesconto(Double valor) {
        if (!ativo || valor == null) {
            return 0.0;
        }
        return valor * (percentual / 100.0);
    }

    @Override
    public String toString() {
        return "Desconto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", percentual=" + percentual +
                ", ativo=" + ativo +
                '}';
    }
}
