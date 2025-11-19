package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.IngredientesRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;

@Repository
public interface IngredientesRepositoryJDBC extends JpaRepository<Ingrediente, Long>, IngredientesRepository {

    @Override
    default List<Ingrediente> recuperaTodos() {
        return findAll();
    }

    @Override
    @Query("SELECT i FROM Ingrediente i JOIN i.receitas r WHERE r.id = ?1")
    List<Ingrediente> recuperaIngredientesReceita(long id);
}
