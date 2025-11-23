package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Repository
public interface CardapioRepositoryJDBC extends JpaRepository<Cardapio, Long>, CardapioRepository {

    @Override
    default Cardapio recuperaPorId(long id) {
        return findById(id).orElse(null);
    }

    @Override
    default Cardapio salvar(Cardapio cardapio) {
        return save(cardapio);
    }

    @Override
    // Por enquanto retorna sempre a pizza de queijo e presunto como indicação do "chef"
    default List<Produto> indicacoesDoChef() {
        Optional<Cardapio> cardapio = findById(1L); // Assumindo que existe cardápio com id 1
        if (cardapio.isPresent() && cardapio.get().getProdutos() != null) {
            return cardapio.get().getProdutos().stream().filter(p -> p.getId() == 2L).toList();
        }
        return List.of();
    }

    @Query("SELECT c FROM Cardapio c")
    List<Cardapio> findAllCardapios();

    @Override
    default List<CabecalhoCardapio> cardapiosDisponiveis() {
        return findAllCardapios().stream()
            .map(c -> new CabecalhoCardapio(c.getId(), c.getTitulo()))
            .toList();
    }
}
