package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AutenticacaoService {
    
    private UsuarioRepository usuarioRepository;
    private SessaoUsuario sessaoUsuario;

    @Autowired
    public AutenticacaoService(UsuarioRepository usuarioRepository, SessaoUsuario sessaoUsuario) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoUsuario = sessaoUsuario;
    }

    /**
     * Registra um novo usuário cliente (sem autenticação)
     */
    public Usuario registrarCliente(String email, String senha, String nome) {
        // Validar email único
        Optional<Usuario> usuarioExistente = usuarioRepository.buscarPorEmail(email);
        if (usuarioExistente.isPresent())
            throw new IllegalArgumentException("Email já cadastrado");
        
        Usuario novoUsuario = new Usuario(email, senha, nome, Usuario.TipoUsuario.CLIENTE);
        return usuarioRepository.salvar(novoUsuario);
    }

    /**
     * Autentica um usuário (login)
     */
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorEmail(email);
        
        if (usuarioOpt.isEmpty())
            throw new IllegalArgumentException("Email ou senha incorretos");
        
        Usuario usuario = usuarioOpt.get();
        
        if (!usuario.isAtivo())
            throw new IllegalArgumentException("Usuário inativo");
       

        if (!usuario.getSenha().equals(senha))
            throw new IllegalArgumentException("Email ou senha incorretos");
        
        // Registrar na sessão
        sessaoUsuario.autenticar(usuario);
        
        return usuario;
    }

    /**
     * Desautentica o usuário (logout)
     */
    public void desautenticar() {
        sessaoUsuario.desautenticar();
    }

    /**
     * Verifica se o usuário está autenticado
     */
    public boolean estaAutenticado() {
        return sessaoUsuario.estaAutenticado();
    }

    /**
     * Obtém o usuário autenticado
     */
    public Usuario getUsuarioAutenticado() {
        if (!estaAutenticado())
            throw new IllegalArgumentException("Nenhum usuário autenticado");
        return sessaoUsuario.getUsuarioAutenticado();
    }
}
