package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Repository
public interface ProdutosRepositoryJDBC extends JpaRepository<Produto, Long>, ProdutosRepository {

    @Override
    default Produto recuperaProdutoPorid(long id) {
        return findById(id).orElse(null);
    }

    @Override
    @Query("SELECT p FROM Produto p WHERE p.cardapio.id = ?1")
    List<Produto> recuperaProdutosCardapio(long cardapioId);
}
