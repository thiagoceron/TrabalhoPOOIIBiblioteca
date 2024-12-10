package org.exemple.biblioteca.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.exemple.biblioteca.model.Livro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Classe de teste para o DAO de Livro.
 * Testa as funcionalidades básicas do DAO, como inserir, buscar, atualizar e deletar.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Permite usar o mesmo estado da classe entre testes.
public class LivroDAOTest {

    private LivroDAO livroDAO; // Instância do DAO que será testado.
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    /**
     * Configuração inicial para criar a tabela no banco de dados antes de executar os testes.
     * Este método é executado uma única vez antes de todos os testes.
     */
    @BeforeAll
    void inicializarBanco() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Cria a tabela "livro" se ela ainda não existir no banco.
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS livro ("
                    + "livroID SERIAL PRIMARY KEY, " // Chave primária com autoincremento.
                    + "titulo VARCHAR(255), "       // Coluna para o título do livro.
                    + "autor VARCHAR(255)"          // Coluna para o autor do livro.
                    + ")");
        }
    }

    /**
     * Método executado antes de cada teste.
     * Inicializa o DAO e limpa os dados da tabela para garantir que cada teste seja independente.
     */
    @BeforeEach
    void setUp() throws SQLException {
        livroDAO = new LivroDAO(); // Instancia o DAO.
        limparDados(); // Limpa os registros existentes na tabela.
    }

    /**
     * Testa a funcionalidade de inserir um livro no banco de dados.
     * Verifica se o livro foi inserido corretamente.
     */
    @Test
    void testInserirLivro() throws SQLException {
        Livro livro = new Livro(1, "Título Teste", "Autor Teste"); // Cria um livro de teste.
        livroDAO.inserir(livro); // Insere o livro no banco.

        List<Livro> livros = livroDAO.buscarTodos(); // Recupera todos os livros do banco.
        // Verifica se o livro com ID 1 está presente.
        assertTrue(livros.stream().anyMatch(l -> l.getLivroID() == 1),
                "O livro inserido não foi encontrado.");
    }

    /**
     * Testa a funcionalidade de buscar todos os livros do banco.
     * Insere dois livros e verifica se a lista retornada contém os itens esperados.
     */
    @Test
    void testBuscarTodos() throws SQLException {
        Livro livro1 = new Livro(2, "Título A", "Autor A");
        Livro livro2 = new Livro(3, "Título B", "Autor B");

        livroDAO.inserir(livro1); // Insere o primeiro livro.
        livroDAO.inserir(livro2); // Insere o segundo livro.

        List<Livro> livros = livroDAO.buscarTodos(); // Recupera todos os livros.
        assertFalse(livros.isEmpty(), "A lista de livros deveria conter itens."); // Verifica se a lista não está vazia.
        assertEquals(2, livros.size(), "A quantidade de livros recuperados não está correta."); // Verifica o tamanho da lista.
    }

    /**
     * Testa a funcionalidade de deletar um livro do banco.
     * Insere um livro, remove-o e verifica se ele foi removido corretamente.
     */
    @Test
    void testDeletarLivro() throws SQLException {
        Livro livro = new Livro(4, "Título Deletar", "Autor Deletar");
        livroDAO.inserir(livro); // Insere o livro no banco.

        livroDAO.deletar(4); // Remove o livro com ID 4.
        List<Livro> livros = livroDAO.buscarTodos(); // Recupera todos os livros.
        // Verifica se o livro com ID 4 não está presente.
        assertTrue(livros.stream().noneMatch(l -> l.getLivroID() == 4),
                "O livro não foi removido corretamente.");
    }

    /**
     * Testa a funcionalidade de atualizar um livro no banco.
     * Insere um livro, atualiza suas informações e verifica se a atualização foi feita corretamente.
     */
    @Test
    void testAtualizarLivro() throws SQLException {
        Livro livro = new Livro(5, "Título Antigo", "Autor Antigo");
        livroDAO.inserir(livro); // Insere o livro inicial.

        Livro livroAtualizado = new Livro(5, "Título Novo", "Autor Novo");
        livroDAO.atualizar(livroAtualizado); // Atualiza o livro com novas informações.

        List<Livro> livros = livroDAO.buscarTodos(); // Recupera todos os livros.
        // Verifica se o livro atualizado está presente com as novas informações.
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("Título Novo") && l.getAutor().equals("Autor Novo")),
                "O livro não foi atualizado corretamente.");
    }

    /**
     * Método auxiliar para limpar os dados da tabela "livro" antes de cada teste.
     * Garante que os testes não sejam influenciados por dados remanescentes.
     */
    private void limparDados() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM livro"); // Remove todos os registros da tabela.
        }
    }
}
