package org.exemple.biblioteca.model;

public class Avaliacao {
    private int avaliacaoID;
    private String livroTitulo;
    private String texto;
    private String nomeUsuario;

    public Avaliacao() {}

    public Avaliacao(int avaliacaoID, String livroTitulo, String texto, String nomeUsuario) {
        this.avaliacaoID = avaliacaoID;
        this.livroTitulo = livroTitulo;
        this.texto = texto;
        this.nomeUsuario = nomeUsuario;
    }

    public int getAvaliacaoID() {
        return avaliacaoID;
    }

    public void setAvaliacaoID(int avaliacaoID) {
        this.avaliacaoID = avaliacaoID;
    }

    public String getLivroTitulo() {
        return livroTitulo;
    }

    public void setLivroTitulo(String livroTitulo) {
        this.livroTitulo = livroTitulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}