package org.exemple.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.exemple.biblioteca.model.Usuario;

public class UsuarioDAO implements IUsuario { // Implementa a interface IUsuario para gerenciar operações do usuário.
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca"; // URL do banco de dados.
    private static final String USER = "postgres"; // Usuário do banco.
    private static final String PASSWORD = "postgres"; // Senha do banco.

    @Override
    public void inserir(Usuario usuario) throws SQLException { // Método para inserir um usuário.
        String sql = "INSERT INTO usuario (usuarioID, nome) VALUES (?, ?)"; // Declaração SQL de inserção.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setInt(1, usuario.getUsuarioID()); // Define o primeiro parâmetro (usuarioID).
            pstmt.setString(2, usuario.getNome()); // Define o segundo parâmetro (nome).
            pstmt.executeUpdate(); // Executa a inserção.
        }
    }

    public List<Usuario> buscarTodos() throws SQLException { // Método para buscar todos os usuários.
        List<Usuario> usuarios = new ArrayList<>(); // Lista para armazenar os resultados.
        String sql = "SELECT * FROM usuario"; // Declaração SQL para buscar todos os registros.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             Statement stmt = conexao.createStatement(); // Cria uma declaração para consulta.
             ResultSet rs = stmt.executeQuery(sql)) { // Executa a consulta.
            while (rs.next()) { // Itera sobre os resultados.
                Usuario usuario = new Usuario(); // Cria um objeto Usuario.
                usuario.setUsuarioID(rs.getInt("usuarioID")); // Define o ID do usuário.
                usuario.setNome(rs.getString("nome")); // Define o nome do usuário.
                usuarios.add(usuario); // Adiciona o usuário à lista.
            }
        }
        return usuarios; // Retorna a lista de usuários.
    }

    public void deletar(int usuarioID) throws SQLException { // Método para deletar um usuário pelo ID.
        String sql = "DELETE FROM usuario WHERE usuarioID = ?"; // Declaração SQL de exclusão.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setInt(1, usuarioID); // Define o parâmetro com o ID do usuário.
            pstmt.executeUpdate(); // Executa a exclusão.
        }
    }



    public boolean usuarioExiste(int usuarioID) throws SQLException { // Método para verificar se o usuário existe.
        String sql = "SELECT COUNT(*) FROM usuario WHERE usuarioID = ?"; // Declaração SQL de contagem.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setInt(1, usuarioID); // Define o parâmetro com o ID do usuário.
            ResultSet rs = pstmt.executeQuery(); // Executa a consulta.
            if (rs.next()) { // Verifica o resultado.
                return rs.getInt(1) > 0; // Retorna true se o usuário existe.
            }
        }
        return false; // Retorna false se o usuário não existe.
    }

    public int obterProximoUsuarioID() throws SQLException { // Método para obter o próximo ID disponível.
        String sql = "SELECT COALESCE(MAX(usuarioID), 0) + 1 AS proximo_id FROM usuario"; // Declaração SQL para o próximo ID.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             Statement stmt = conexao.createStatement(); // Cria uma declaração.
             ResultSet rs = stmt.executeQuery(sql)) { // Executa a consulta.
            if (rs.next()) { // Verifica o resultado.
                return rs.getInt("proximo_id"); // Retorna o próximo ID.
            }
        }
        return 1; // Retorna 1 caso a tabela esteja vazia.
    }

    public void atualizar(Usuario usuario) throws SQLException { // Método para atualizar um usuário.
        String sql = "UPDATE usuario SET nome = ? WHERE usuarioID = ?"; // Declaração SQL de atualização.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setString(1, usuario.getNome()); // Define o parâmetro nome.
            pstmt.setInt(2, usuario.getUsuarioID()); // Define o parâmetro ID.
            pstmt.executeUpdate(); // Executa a atualização.
        }
    }
}
