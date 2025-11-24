package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@Repository
public interface DescontoRepositoryJDBC extends JpaRepository<Desconto, Long> {

    // Método para buscar desconto pelo código
    Desconto findByCodigoIgnoreCase(String codigo);
}
