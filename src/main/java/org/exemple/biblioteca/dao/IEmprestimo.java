package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import org.exemple.biblioteca.model.Emprestimo;

public interface IEmprestimo { // Declara a interface IEmprestimo, que define métodos para operações em empréstimos.
    void inserir(Emprestimo emprestimo) throws SQLException; // Declara o método de inserção de um empréstimo.
}
