package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import org.exemple.biblioteca.model.Livro;

public interface ILivro { // Declara a interface ILivro, que define métodos para operações em livros.
    void inserir(Livro livro) throws SQLException; // Declara o método de inserção de um livro.
    int obterProximoLivroID() throws SQLException; // Declara o método para obter o próximo ID disponível.
}
