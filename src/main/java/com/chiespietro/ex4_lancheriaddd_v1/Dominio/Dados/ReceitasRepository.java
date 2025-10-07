package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

public interface ReceitasRepository {
    Receita recuperaReceita(long id);
    
}
