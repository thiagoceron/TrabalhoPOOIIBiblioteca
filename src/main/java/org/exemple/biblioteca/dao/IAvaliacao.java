package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import org.exemple.biblioteca.model.Avaliacao;

public interface IAvaliacao { // Declara a interface IAvaliacao, que define métodos para operações em avaliações.
    void inserir(Avaliacao avaliacao) throws SQLException; // Declara o método de inserção de uma avaliação.
    void atualizar(Avaliacao avaliacao) throws SQLException; // Declara o método de atualização de uma avaliação.
    void deletar(int avaliacaoID) throws SQLException; // Declara o método para deletar uma avaliação pelo ID.
    int obterProximoAvaliacaoID() throws SQLException; // Declara o método para obter o próximo ID de avaliação disponível.
}
