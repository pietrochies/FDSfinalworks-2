package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;

@Embeddable
public record CabecalhoCardapio(long id, String titulo) { }

