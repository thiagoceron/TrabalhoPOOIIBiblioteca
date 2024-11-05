package org.exemple.biblioteca.dao;

import java.sql.SQLException;
import java.util.List;
import org.exemple.biblioteca.model.Usuario;

public interface IUsuario {
    void inserir(Usuario usuario) throws SQLException; // Método para inserir um usuário.
    List<Usuario> buscarTodos() throws SQLException; // Método para buscar todos os usuários.
    void deletar(int usuarioID) throws SQLException; // Método para deletar um usuário pelo ID.
    List<Usuario> searchByName(String searchTerm) throws SQLException; // Método para buscar usuários pelo nome.
    boolean usuarioExiste(int usuarioID) throws SQLException; // Método para verificar se o usuário existe.
    int obterProximoUsuarioID() throws SQLException; // Método para obter o próximo ID disponível.
    void atualizar(Usuario usuario) throws SQLException; // Método para atualizar um usuário.
}
