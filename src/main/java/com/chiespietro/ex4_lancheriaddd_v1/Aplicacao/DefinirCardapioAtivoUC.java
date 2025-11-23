package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Component
public class DefinirCardapioAtivoUC {
    
    private CardapioRepository cardapioRepository;
    
    @Autowired
    public DefinirCardapioAtivoUC(CardapioRepository cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }
    
    public Cardapio executar(Usuario master, long cardapioId) throws Exception {
        // Validar se é usuário MASTER
        if (!Usuario.TipoUsuario.MASTER.equals(master.getTipoUsuario())) {
            throw new IllegalArgumentException("Apenas usuários MASTER podem definir cardápio ativo");
        }
        
        // Validar se o usuário está ativo
        if (!master.isAtivo()) {
            throw new IllegalArgumentException("Usuário inativo não pode realizar esta ação");
        }
        
        // Recuperar o cardápio
        Cardapio cardapio = cardapioRepository.recuperaPorId(cardapioId);
        if (cardapio == null) {
            throw new IllegalArgumentException("Cardápio não encontrado");
        }
        
        // Definir como ativo
        cardapio.setAtivo(true);
        
        // Salvar alterações
        return cardapioRepository.salvar(cardapio);
    }
}
