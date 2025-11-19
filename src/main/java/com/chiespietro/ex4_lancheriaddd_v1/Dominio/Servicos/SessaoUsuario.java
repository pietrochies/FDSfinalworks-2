package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.io.Serializable;

@Component
@SessionScope
public class SessaoUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioAutenticado;

    public void autenticar(Usuario usuario) {
        if (usuario == null || !usuario.isAtivo())
            throw new IllegalArgumentException("Usuário inválido ou inativo");
        this.usuarioAutenticado = usuario;
    }

    public void desautenticar() {
        this.usuarioAutenticado = null;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public boolean estaAutenticado() {
        return usuarioAutenticado != null;
    }

    public boolean isMaster() {
        return estaAutenticado() && usuarioAutenticado.getTipoUsuario() == Usuario.TipoUsuario.MASTER;
    }

    public boolean isCliente() {
        return estaAutenticado() && usuarioAutenticado.getTipoUsuario() == Usuario.TipoUsuario.CLIENTE;
    }
}
