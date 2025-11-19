package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs;

import com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public class UsuarioResponse {
    private long id;
    private String email;
    private String nome;
    private String tipoUsuario;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.nome = usuario.getNome();
        this.tipoUsuario = usuario.getTipoUsuario().toString();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
}
