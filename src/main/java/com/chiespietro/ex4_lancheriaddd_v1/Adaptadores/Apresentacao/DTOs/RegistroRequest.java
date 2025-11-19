package com.chiespietro.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.DTOs;

public class RegistroRequest {
    private String email;
    private String senha;
    private String nome;

    public RegistroRequest() {}

    public RegistroRequest(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
