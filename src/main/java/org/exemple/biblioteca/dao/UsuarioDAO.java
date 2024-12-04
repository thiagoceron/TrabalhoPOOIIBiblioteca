package org.exemple.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.exemple.biblioteca.model.Usuario;

public class UsuarioDAO implements IUsuario {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    // Método genérico para executar uma consulta com PreparedStatement
    // A ideia é reutilizar a lógica de conexão e execução de SQL para qualquer operação
    private <T> T executarConsulta(String sql, SQLFunction<PreparedStatement, T> func) throws SQLException {
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Estabelece a conexão com o banco de dados
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração SQL
            return func.apply(pstmt); // Aplica a função fornecida com o PreparedStatement
        }
    }

    @Override
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (usuarioID, nome) VALUES (?, ?)"; // SQL para inserir usuário
        // Chama o método genérico executarConsulta passando a lógica para definir parâmetros e executar a inserção
        executarConsulta(sql, pstmt -> {
            pstmt.setInt(1, usuario.getUsuarioID()); // Define o ID do usuário
            pstmt.setString(2, usuario.getNome()); // Define o nome do usuário
            pstmt.executeUpdate(); // Executa a inserção
            return null; // Não há retorno, já que a inserção não precisa de dados de volta
        });
    }

    public List<Usuario> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM usuario"; // SQL para buscar todos os usuários
        // Chama o método genérico para executar a consulta e processar os resultados
        return executarConsulta(sql, pstmt -> {
            ResultSet rs = pstmt.executeQuery(); // Executa a consulta
            List<Usuario> usuarios = new ArrayList<>(); // Lista para armazenar os usuários
            while (rs.next()) { // Itera sobre os resultados
                Usuario usuario = new Usuario(); // Cria um novo objeto Usuario
                usuario.setUsuarioID(rs.getInt("usuarioID")); // Define o ID do usuário
                usuario.setNome(rs.getString("nome")); // Define o nome do usuário
                usuarios.add(usuario); // Adiciona o usuário à lista
            }
            return usuarios; // Retorna a lista de usuários
        });
    }

    public void deletar(int usuarioID) throws SQLException {
        String sql = "DELETE FROM usuario WHERE usuarioID = ?"; // SQL para deletar usuário
        // Chama o método genérico para executar a exclusão
        executarConsulta(sql, pstmt -> {
            pstmt.setInt(1, usuarioID); // Define o ID do usuário a ser excluído
            pstmt.executeUpdate(); // Executa a exclusão
            return null; // Não há retorno necessário
        });
    }

    public boolean usuarioExiste(int usuarioID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE usuarioID = ?"; // SQL para contar se o usuário existe
        // Chama o método genérico para verificar a existência do usuário
        return executarConsulta(sql, pstmt -> {
            pstmt.setInt(1, usuarioID); // Define o ID do usuário
            ResultSet rs = pstmt.executeQuery(); // Executa a consulta
            return rs.next() && rs.getInt(1) > 0; // Retorna true se o usuário existir
        });
    }

    // Função auxiliar para obter o próximo ID do usuário
    public int obterProximoUsuarioID() throws SQLException {
        String sql = "SELECT COALESCE(MAX(usuarioID), 0) + 1 AS proximo_id FROM usuario"; // SQL para obter o próximo ID disponível
        // Chama o método genérico para obter o próximo ID
        return executarConsulta(sql, pstmt -> {
            ResultSet rs = pstmt.executeQuery(); // Executa a consulta
            return rs.next() ? rs.getInt("proximo_id") : 1; // Retorna o próximo ID ou 1 se não houver usuários
        });
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ? WHERE usuarioID = ?"; // SQL para atualizar o nome do usuário
        // Chama o método genérico para executar a atualização
        executarConsulta(sql, pstmt -> {
            pstmt.setString(1, usuario.getNome()); // Define o nome do usuário a ser atualizado
            pstmt.setInt(2, usuario.getUsuarioID()); // Define o ID do usuário
            pstmt.executeUpdate(); // Executa a atualização
            return null; // Não há retorno necessário
        });
    }

    // Interface funcional para facilitar o uso de lambdas
    // Permite que passagens de funções sejam feitas como argumentos, simplificando o código
    @FunctionalInterface
    private interface SQLFunction<T, R> {
        R apply(T t) throws SQLException; // Método que recebe um parâmetro do tipo T e retorna um valor do tipo R
    }
}
