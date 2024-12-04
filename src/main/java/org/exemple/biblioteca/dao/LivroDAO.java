package org.exemple.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.exemple.biblioteca.model.Livro;

public class LivroDAO implements ILivro { // Implementa a interface ILivro para gerenciar livros.
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca"; // URL do banco de dados.
    private static final String USER = "postgres"; // Usuário do banco.
    private static final String PASSWORD = "postgres"; // Senha do banco.

    @Override
    public void inserir(Livro livro) throws SQLException { // Método para inserir um livro.
        String sql = "INSERT INTO livro (livroID, titulo, autor) VALUES (?, ?, ?)"; // Declaração SQL de inserção.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setInt(1, livro.getLivroID()); // Define o ID do livro.
            pstmt.setString(2, livro.getTitulo()); // Define o título.
            pstmt.setString(3, livro.getAutor()); // Define o autor.
            pstmt.executeUpdate(); // Executa a inserção.
        }
    }

    public List<Livro> buscarTodos() throws SQLException { // Método para buscar todos os livros.
        List<Livro> livros = new ArrayList<>(); // Lista para armazenar os resultados.
        String sql = "SELECT * FROM livro"; // Declaração SQL para buscar todos os livros.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             Statement stmt = conexao.createStatement(); // Cria uma declaração de consulta.
             ResultSet rs = stmt.executeQuery(sql)) { // Executa a consulta.
            while (rs.next()) { // Itera sobre os resultados.
                Livro livro = new Livro(); // Cria um objeto Livro.
                livro.setLivroID(rs.getInt("livroID")); // Define o ID do livro.
                livro.setTitulo(rs.getString("titulo")); // Define o título.
                livro.setAutor(rs.getString("autor")); // Define o autor.
                livros.add(livro); // Adiciona o livro à lista.
            }
        }
        return livros; // Retorna a lista de livros.
    }

    public void deletar(int livroID) throws SQLException { // Método para deletar um livro pelo ID.
        String sql = "DELETE FROM livro WHERE livroID = ?"; // Declaração SQL de exclusão.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setInt(1, livroID); // Define o ID do livro.
            pstmt.executeUpdate(); // Executa a exclusão.
        }
    }

    public void atualizar(Livro livro) throws SQLException { // Método para atualizar informações do livro.
        String sql = "UPDATE livro SET titulo = ?, autor = ? WHERE livroID = ?"; // Declaração SQL de atualização.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD); // Conecta ao banco.
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // Prepara a declaração.
            pstmt.setString(1, livro.getTitulo()); // Define o título do livro.
            pstmt.setString(2, livro.getAutor()); // Define o autor do livro.
            pstmt.setInt(3, livro.getLivroID()); // Define o ID do livro.
            pstmt.executeUpdate(); // Executa a atualização.
        }
    }

    public int obterProximoLivroID() throws SQLException {
        // Define a instrução SQL para obter o próximo ID de livro, utilizando COALESCE para lidar com a ausência de registros
        String sql = "SELECT COALESCE(MAX(livroID), 0) + 1 AS proximo_id FROM livro";

        // Tenta estabelecer uma conexão com o banco de dados e executar a consulta
        try (Connection conexao = criarConexao(); // Método para criar a conexão com o banco de dados
             Statement stmt = conexao.createStatement(); // Cria um Statement para executar a consulta
             ResultSet rs = stmt.executeQuery(sql)) { // Executa a consulta e obtém o ResultSet

            // Retorna o próximo ID de livro, ou 1 se não houver livros na tabela
            return rs.next() ? rs.getInt("proximo_id") : 1;
        }
    }

    private Connection criarConexao() throws SQLException {
        // Cria e retorna uma nova conexão com o banco de dados usando as credenciais fornecidas
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
