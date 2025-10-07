package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados;

import java.util.List;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public interface CardapioRepository {
    List<CabecalhoCardapio> cardapiosDisponiveis();
    Cardapio recuperaPorId(long id);
    List<Produto> indicacoesDoChef();
}
