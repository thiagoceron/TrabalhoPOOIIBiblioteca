package org.exemple.biblioteca.dao;


import org.exemple.biblioteca.model.Usuario;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    // Teste para verificar a inserção de um novo usuário no banco de dados
    @Test
    public void testeInsercao() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO(); // Criação do objeto DAO para interagir com o banco
        Usuario novoUsuario = new Usuario(); // Criação de um novo objeto de usuário
        int novoID = usuarioDAO.obterProximoUsuarioID(); // Obtém o próximo ID disponível
        novoUsuario.setUsuarioID(novoID); // Define o ID do usuário
        novoUsuario.setNome("Teste Usuario"); // Define o nome do usuário
        usuarioDAO.inserir(novoUsuario); // Insere o usuário no banco

        // Busca o usuário recém-inserido e verifica se ele está presente
        Usuario usuarioInserido = usuarioDAO.buscarTodos().stream()
                .filter(u -> u.getUsuarioID() == novoID) // Filtra pelo ID do novo usuário
                .findFirst() // Retorna o primeiro resultado (ou nenhum)
                .orElse(null); // Retorna null se não encontrar

        // Verifica se o usuário foi inserido corretamente
        assertNotNull(usuarioInserido, "Usuário não foi inserido corretamente.");
        assertEquals("Teste Usuario", usuarioInserido.getNome(), "Nome do usuário inserido não corresponde.");
    }

    // Teste para verificar se a busca de todos os usuários retorna resultados
    @Test
    public void testeBusca() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO(); // Criação do objeto DAO
        List<Usuario> usuarios = usuarioDAO.buscarTodos(); // Busca todos os usuários

        // Verifica se a lista não é nula e contém pelo menos um usuário
        assertNotNull(usuarios, "A lista de usuários não deveria ser nula.");
        assertTrue(usuarios.size() > 0, "A lista de usuários deveria conter ao menos um elemento.");
    }

    // Teste para verificar a atualização de um usuário existente
    @Test
    public void testeAtualizacao() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();  // Cria uma instância do DAO (Data Access Object) que gerencia os usuários no banco de dados

        // Preparação: Insere um usuário caso a lista esteja vazia
        if (usuarioDAO.buscarTodos().isEmpty()) {  // Verifica se não há usuários cadastrados no banco de dados
            Usuario usuarioInicial = new Usuario();  // Cria um novo objeto Usuario
            usuarioInicial.setUsuarioID(usuarioDAO.obterProximoUsuarioID());  // Define o ID do usuário com o próximo valor disponível no banco
            usuarioInicial.setNome("Usuario Inicial");  // Define o nome do novo usuário
            usuarioDAO.inserir(usuarioInicial);  // Insere o novo usuário no banco de dados
        }

        // Teste: Atualiza o primeiro usuário da lista
        List<Usuario> usuarios = usuarioDAO.buscarTodos();  // Busca todos os usuários do banco de dados
        assertFalse(usuarios.isEmpty(), "Não há usuários para atualizar.");  // Verifica se a lista de usuários não está vazia, ou seja, se há usuários para atualizar

        Usuario usuarioParaAtualizar = usuarios.get(0);  // Seleciona o primeiro usuário da lista de usuários
        String nomeAntigo = usuarioParaAtualizar.getNome();  // Armazena o nome antigo do usuário antes de atualizar
        usuarioParaAtualizar.setNome("Usuario Atualizado");  // Atualiza o nome do usuário para "Usuario Atualizado"
        usuarioDAO.atualizar(usuarioParaAtualizar);  // Chama o método de atualização no DAO para salvar a alteração no banco de dados

        // Realiza uma nova busca para confirmar a atualização
        Usuario usuarioAtualizado = usuarioDAO.buscarTodos().stream()  // Busca todos os usuários novamente para verificar se a atualização foi realizada
                .filter(u -> u.getUsuarioID() == usuarioParaAtualizar.getUsuarioID())  // Filtra a lista de usuários para encontrar o usuário que foi atualizado (pelo ID)
                .findFirst()  // Pega o primeiro usuário encontrado
                .orElse(null);  // Caso não encontre o usuário, retorna null

        // Verifica se o usuário foi atualizado corretamente
        assertNotNull(usuarioAtualizado, "Usuário atualizado não foi encontrado.");  // Verifica se o usuário atualizado foi encontrado (não pode ser null)
        assertEquals("Usuario Atualizado", usuarioAtualizado.getNome(), "O nome do usuário atualizado não corresponde.");  // Verifica se o nome do usuário foi alterado para "Usuario Atualizado"
    }




    // Teste para verificar a exclusão de um usuário
    @Test
    public void testeExclusao() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO(); // Criação do objeto DAO
        List<Usuario> usuarios = usuarioDAO.buscarTodos(); // Busca todos os usuários
        assertFalse(usuarios.isEmpty(), "Não há usuários para excluir."); // Verifica se há usuários na lista

        int idParaExcluir = usuarios.get(usuarios.size() - 1).getUsuarioID(); // Seleciona o ID do último usuário
        usuarioDAO.deletar(idParaExcluir); // Exclui o usuário pelo ID

        // Verifica se o usuário foi realmente excluído
        boolean usuarioExiste = usuarioDAO.usuarioExiste(idParaExcluir); // Verifica se o usuário ainda existe
        assertFalse(usuarioExiste, "O usuário não foi excluído corretamente."); // Confirma a exclusão
    }
}