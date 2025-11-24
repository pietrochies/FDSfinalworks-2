package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios;

import java.util.List;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

public interface DescontoRepository {
    
    // Recupera um desconto pelo código
    Desconto findByCodigo(String codigo);

    // Recupera um desconto pelo id
    Desconto findById(Long id);

    // Salva/atualiza um desconto
    void save(Desconto desconto);

    // Recupera todos os descontos
    List<Desconto> findAll();
}
