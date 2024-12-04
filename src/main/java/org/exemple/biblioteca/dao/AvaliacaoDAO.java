package org.exemple.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.exemple.biblioteca.model.Avaliacao;

public class AvaliacaoDAO implements IAvaliacao {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca"; // URL do banco de dados.
    private static final String USER = "postgres"; // Usuário do banco de dados.
    private static final String PASSWORD = "postgres"; // Senha do banco de dados.

    @Override
    public void inserir(Avaliacao avaliacao) throws SQLException {
        String sql = "INSERT INTO avaliacao (avaliacaoID, livroTitulo, texto, nomeUsuario) VALUES (?, ?, ?, ?)"; // Query para inserir uma avaliação.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, avaliacao.getAvaliacaoID()); // Define o ID da avaliação.
            pstmt.setString(2, avaliacao.getLivroTitulo()); // Define o título do livro.
            pstmt.setString(3, avaliacao.getTexto()); // Define o texto da avaliação.
            pstmt.setString(4, avaliacao.getNomeUsuario()); // Define o nome do usuário.
            pstmt.executeUpdate(); // Executa a inserção.
        }
    }

    public List<Avaliacao> buscarTodos() throws SQLException {
        List<Avaliacao> avaliacoes = new ArrayList<>(); // Cria lista para armazenar as avaliações.
        String sql = "SELECT * FROM avaliacao"; // Consulta para buscar todas as avaliações.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { // Itera sobre o resultado da consulta.
                Avaliacao avaliacao = new Avaliacao(); // Cria um novo objeto Avaliacao.
                avaliacao.setAvaliacaoID(rs.getInt("avaliacaoID")); // Define o ID da avaliação.
                avaliacao.setLivroTitulo(rs.getString("livroTitulo")); // Define o título do livro.
                avaliacao.setTexto(rs.getString("texto")); // Define o texto da avaliação.
                avaliacao.setNomeUsuario(rs.getString("nomeUsuario")); // Define o nome do usuário.
                avaliacoes.add(avaliacao); // Adiciona a avaliação à lista.
            }
        }
        return avaliacoes; // Retorna a lista de avaliações.
    }

    public void deletar(int avaliacaoID) throws SQLException {
        final String SQL_DELETE = "DELETE FROM avaliacao WHERE avaliacaoID = ?";

        try (Connection conexao = criarConexao(); // Método centralizado para obter conexões.
             PreparedStatement pstmt = conexao.prepareStatement(SQL_DELETE)) {

            pstmt.setInt(1, avaliacaoID); // Configura o parâmetro.
            int linhasAfetadas = pstmt.executeUpdate(); // Executa a exclusão.

            if (linhasAfetadas == 0) {
                throw new SQLException("Nenhuma avaliação encontrada com o ID fornecido: " + avaliacaoID);
            }
        }
    }

    private Connection criarConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    @Override
    public void atualizar(Avaliacao avaliacao) throws SQLException {
        String sql = "UPDATE avaliacao SET livroTitulo = ?, texto = ?, nomeUsuario = ? WHERE avaliacaoID = ?"; // Query para atualizar uma avaliação.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, avaliacao.getLivroTitulo()); // Define o título do livro.
            pstmt.setString(2, avaliacao.getTexto()); // Define o texto da avaliação.
            pstmt.setString(3, avaliacao.getNomeUsuario()); // Define o nome do usuário.
            pstmt.setInt(4, avaliacao.getAvaliacaoID()); // Define o ID da avaliação.
            pstmt.executeUpdate(); // Executa a atualização.
        }
    }

    public int obterProximoAvaliacaoID() throws SQLException {
        String sql = "SELECT COALESCE(MAX(avaliacaoID), 0) + 1 AS proximo_id FROM avaliacao";

        try (Connection conexao = criarConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getInt("proximo_id") : 1; // Retorna o próximo ID ou 1 se não houver avaliações.
        }
    }

}
