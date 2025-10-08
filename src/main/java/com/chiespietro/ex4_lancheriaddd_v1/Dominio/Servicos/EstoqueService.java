package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;


public class EstoqueService {
    public boolean temEstoqueSuficiente(List<ItemPedido> itens) {
        // Sempre retorna true, ignorando os itens
        return true;
    }
}
