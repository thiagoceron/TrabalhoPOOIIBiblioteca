package org.exemple.biblioteca.model;

public class Usuario {
    private int usuarioID;
    private String nome;

    public Usuario() {}

    public Usuario(int usuarioID, String nome) {
        this.usuarioID = usuarioID;
        this.nome = nome;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}