package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import java.util.List;
import org.exemple.biblioteca.model.Emprestimo;

public interface IEmprestimo {
    void inserir(Emprestimo emprestimo) throws SQLException; // Método para inserir um empréstimo.
    List<Emprestimo> buscarTodos() throws SQLException; // Método para buscar todos os empréstimos.
    void deletar(int id) throws SQLException; // Método para deletar um empréstimo pelo ID.
}
