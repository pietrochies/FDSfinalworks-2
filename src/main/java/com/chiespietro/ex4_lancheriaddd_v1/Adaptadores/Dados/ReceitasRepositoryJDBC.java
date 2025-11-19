package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.ReceitasRepository;

@Repository
public interface ReceitasRepositoryJDBC extends JpaRepository<Receita, Long>, ReceitasRepository {

    @Override
    default Receita recuperaReceita(long id) {
        return findById(id).orElse(null);
    }

}


