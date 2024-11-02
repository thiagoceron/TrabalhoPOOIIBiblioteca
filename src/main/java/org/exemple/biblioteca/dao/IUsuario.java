package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import org.exemple.biblioteca.model.Usuario;

public interface IUsuario { // Declara a interface IUsuario, que define métodos para operações em usuários.
    void inserir(Usuario usuario) throws SQLException; // Declara o método de inserção de um usuário.
}
