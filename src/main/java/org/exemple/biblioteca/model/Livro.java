package org.exemple.biblioteca.model;

public class Livro {
    private int livroID;
    private String titulo;
    private String autor;

    public Livro() {}

    public Livro(int livroID, String titulo, String autor) {
        this.livroID = livroID;
        this.titulo = titulo;
        this.autor = autor;
    }

    public int getLivroID() {
        return livroID;
    }

    public void setLivroID(int livroID) {
        this.livroID = livroID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}