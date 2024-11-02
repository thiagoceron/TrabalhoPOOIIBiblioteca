package org.exemple.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.exemple.biblioteca.model.Emprestimo;
import org.exemple.biblioteca.model.Livro;
import org.exemple.biblioteca.model.Usuario;

public class EmprestimoDAO implements IEmprestimo {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca"; // URL do banco de dados.
    private static final String USER = "postgres"; // Usuário do banco de dados.
    private static final String PASSWORD = "postgres"; // Senha do banco de dados.

    @Override
    public void inserir(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (usuarioID, livroID, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)"; // Query para inserir um empréstimo.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, emprestimo.getUsuario().getUsuarioID()); // Define o ID do usuário.
            pstmt.setInt(2, emprestimo.getLivro().getLivroID()); // Define o ID do livro.
            pstmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo())); // Define a data do empréstimo.
            pstmt.setDate(4, emprestimo.getDataDevolucao() != null ? Date.valueOf(emprestimo.getDataDevolucao()) : null); // Define a data de devolução.
            pstmt.executeUpdate(); // Executa a inserção no banco de dados.
        }
    }

    public List<Emprestimo> buscarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>(); // Cria lista para armazenar os empréstimos.
        String sql = "SELECT e.*, u.nome AS usuario_nome, l.titulo AS livro_titulo " +
                "FROM emprestimo e " +
                "JOIN usuario u ON e.usuarioID = u.usuarioID " +
                "JOIN livro l ON e.livroID = l.livroID"; // Consulta para buscar todos os empréstimos com detalhes.

        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { // Itera sobre o resultado da consulta.
                Emprestimo emprestimo = new Emprestimo(); // Cria um novo objeto Emprestimo.
                emprestimo.setEmprestimoID(rs.getInt("emprestimoID")); // Define o ID do empréstimo.
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate()); // Define a data de empréstimo.
                emprestimo.setDataDevolucao(rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null); // Define a data de devolução, se houver.

                Usuario usuario = new Usuario(); // Cria um novo objeto Usuario.
                usuario.setUsuarioID(rs.getInt("usuarioID")); // Define o ID do usuário.
                usuario.setNome(rs.getString("usuario_nome")); // Define o nome do usuário.
                emprestimo.setUsuario(usuario); // Associa o usuário ao empréstimo.

                Livro livro = new Livro(); // Cria um novo objeto Livro.
                livro.setLivroID(rs.getInt("livroID")); // Define o ID do livro.
                livro.setTitulo(rs.getString("livro_titulo")); // Define o título do livro.
                emprestimo.setLivro(livro); // Associa o livro ao empréstimo.

                emprestimos.add(emprestimo); // Adiciona o empréstimo à lista.
            }
        }
        return emprestimos; // Retorna a lista de empréstimos.
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM emprestimo WHERE emprestimoID = ?"; // Query para deletar um empréstimo.
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id); // Define o ID do empréstimo a ser deletado.
            stmt.executeUpdate(); // Executa a exclusão.
        }
    }
}
