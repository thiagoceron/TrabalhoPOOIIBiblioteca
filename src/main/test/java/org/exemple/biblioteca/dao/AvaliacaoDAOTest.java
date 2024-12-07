package org.exemple.biblioteca.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.exemple.biblioteca.model.Avaliacao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Classe de teste para o DAO de Avaliação.
 *
 * Esta classe utiliza o framework JUnit para validar as operações
 * de inserção, exclusão, busca e gerenciamento de IDs na tabela de avaliações
 * de um banco de dados PostgreSQL.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AvaliacaoDAOTest {

    private AvaliacaoDAO avaliacaoDAO;
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    /**
     * Configura o banco de dados antes da execução dos testes.
     * Cria a tabela de avaliação caso ela ainda não exista.
     */
    @BeforeAll
    void inicializarBanco() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS avaliacao ("
                    + "avaliacao_id SERIAL PRIMARY KEY, "
                    + "titulo VARCHAR(255), "
                    + "texto TEXT, "
                    + "usuario VARCHAR(100)"
                    + ")");
        }
    }

    /**
     * Configura o ambiente antes de cada teste.
     * Instancia o objeto AvaliacaoDAO e limpa os dados da tabela.
     */
    @BeforeEach
    void setUp() throws SQLException {
        avaliacaoDAO = new AvaliacaoDAO();
        limparDados();
    }

    /**
     * Testa o método de inserção de uma avaliação.
     * Valida se a avaliação inserida pode ser recuperada corretamente.
     */
    @Test
    void testInserirAvaliacao() throws SQLException {
        Avaliacao avaliacao = new Avaliacao(1, "Livro Teste", "Texto de avaliação", "Usuário Teste");
        avaliacaoDAO.inserir(avaliacao);

        List<Avaliacao> avaliacoes = avaliacaoDAO.buscarTodos();
        assertTrue(avaliacoes.stream().anyMatch(a -> a.getAvaliacaoID() == 1),
                "A avaliação inserida não foi encontrada.");
    }

    /**
     * Testa o método que retorna todas as avaliações.
     * Valida se as avaliações inseridas são retornadas corretamente.
     */
    @Test
    void testBuscarTodos() throws SQLException {
        Avaliacao avaliacao1 = new Avaliacao(2, "Livro A", "Texto A", "Usuário A");
        Avaliacao avaliacao2 = new Avaliacao(3, "Livro B", "Texto B", "Usuário B");

        avaliacaoDAO.inserir(avaliacao1);
        avaliacaoDAO.inserir(avaliacao2);

        List<Avaliacao> avaliacoes = avaliacaoDAO.buscarTodos();
        assertFalse(avaliacoes.isEmpty(), "A lista de avaliações deveria conter itens.");
    }

    /**
     * Testa o método de exclusão de uma avaliação.
     * Verifica se a avaliação é removida corretamente da tabela.
     */
    @Test
    void testDeletarAvaliacao() throws SQLException {
        Avaliacao avaliacao = new Avaliacao(4, "Livro Deletar", "Texto Deletar", "Usuário Deletar");
        avaliacaoDAO.inserir(avaliacao);

        avaliacaoDAO.deletar(4);
        List<Avaliacao> avaliacoes = avaliacaoDAO.buscarTodos();
        assertTrue(avaliacoes.stream().noneMatch(a -> a.getAvaliacaoID() == 4),
                "A avaliação não foi removida corretamente.");
    }

    /**
     * Testa o método que obtém o próximo ID de avaliação.
     * Verifica se o próximo ID gerado é maior que 0.
     */
    @Test
    void testObterProximoAvaliacaoID() throws SQLException {
        int proximoID = avaliacaoDAO.obterProximoAvaliacaoID();
        assertTrue(proximoID > 0, "O próximo ID não é maior que 0.");
    }

    /**
     * Método auxiliar para limpar os dados da tabela de avaliações.
     */
    private void limparDados() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM avaliacao");
        }
    }
}