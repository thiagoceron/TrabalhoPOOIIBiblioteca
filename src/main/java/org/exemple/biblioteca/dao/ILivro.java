package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import java.util.List;
import org.exemple.biblioteca.model.Livro;

public interface ILivro {
    void inserir(Livro livro) throws SQLException; // Método para inserir um livro.
    List<Livro> buscarTodos() throws SQLException; // Método para buscar todos os livros.
    void deletar(int livroID) throws SQLException; // Método para deletar um livro pelo ID.
    void atualizar(Livro livro) throws SQLException; // Método para atualizar informações do livro.
    int obterProximoLivroID() throws SQLException; // Método para obter o próximo ID disponível.
}
