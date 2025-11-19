package com.chiespietro.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    public enum TipoUsuario {
        MASTER, CLIENTE
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "senha", nullable = false)
    private String senha;
    
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;
    
    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    public Usuario() {}

    public Usuario(String email, String senha, String nome, TipoUsuario tipoUsuario) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email inválido");
        if (senha == null || senha.length() < 6)
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome inválido");
        if (tipoUsuario == null)
            throw new IllegalArgumentException("Tipo de usuário inválido");
        
        this.email = email.toLowerCase();
        this.senha = senha;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.length() < 6)
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome inválido");
        this.nome = nome;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", email=" + email + ", nome=" + nome + 
               ", tipoUsuario=" + tipoUsuario + ", ativo=" + ativo + "]";
    }
}
