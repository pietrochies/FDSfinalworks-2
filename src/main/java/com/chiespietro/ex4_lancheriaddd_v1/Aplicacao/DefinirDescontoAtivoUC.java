package com.chiespietro.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Repositorios.DescontoRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;

@Component
public class DefinirDescontoAtivoUC {

    private final DescontoRepository descontoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public DefinirDescontoAtivoUC(DescontoRepository descontoRepository, UsuarioRepository usuarioRepository) {
        this.descontoRepository = descontoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Define qual desconto está ativo no momento.
     * Apenas usuários com perfil "master" podem executar esta ação.
     * 
     * @param usuarioId ID do usuário que está realizando a ação
     * @param codigoDescontoAtivo Código do desconto que deve ser ativado
     * @throws IllegalArgumentException Se o usuário não é master ou desconto não encontrado
     */
    public void executar(Long usuarioId, String codigoDescontoAtivo) {
        // Valida se o usuário é master
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId).orElse(null);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + usuarioId);
        }

        if (!usuario.isPerfil("master")) {
            throw new IllegalArgumentException("Apenas usuários master podem definir desconto ativo");
        }

        // Busca o desconto a ser ativado
        Desconto descontoParaAtivar = descontoRepository.findByCodigo(codigoDescontoAtivo);
        if (descontoParaAtivar == null) {
            throw new IllegalArgumentException("Desconto não encontrado: " + codigoDescontoAtivo);
        }

        // Desativa todos os outros descontos
        inativarTodosOsDescontos();

        // Ativa o desconto escolhido
        descontoParaAtivar.setAtivo(true);
        descontoRepository.save(descontoParaAtivar);

        System.out.println("Desconto ativo alterado para: " + codigoDescontoAtivo);
    }

    /**
     * Inativa todos os descontos disponíveis
     */
    private void inativarTodosOsDescontos() {
        // Busca todos os descontos e desativa
        for (Desconto desconto : descontoRepository.findAll()) {
            desconto.setAtivo(false);
            descontoRepository.save(desconto);
        }
    }
}
