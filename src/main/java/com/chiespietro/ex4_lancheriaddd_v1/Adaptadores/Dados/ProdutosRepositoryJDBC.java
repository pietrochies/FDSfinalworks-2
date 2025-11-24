package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
    default List<Produto> recuperaProdutosCardapio(long cardapioId) {
        // Produtos agora estão relacionados a cardápios via tabela de junção (cardapio_produto)
        // Você deve usar CardapioRepository para recuperar o cardápio com seus produtos
        return List.of();
    }
}
