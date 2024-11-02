package org.exemple.biblioteca.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.exemple.biblioteca.dao.UsuarioDAO;
import org.exemple.biblioteca.model.Usuario;

import java.sql.SQLException;

public class IncluiUsuarioController {
    @FXML
    private TextField txtNomeUsuario;
    @FXML
    private Button btnConf;
    @FXML
    private TableView<Usuario> tabelaUsuarios;
    @FXML
    private TableColumn<Usuario, String> colunaUsuarioID;
    @FXML
    private TableColumn<Usuario, String> colunaNome;

    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> listaUsuarios;

    public IncluiUsuarioController() {
        usuarioDAO = new UsuarioDAO(); // Inicializa o DAO para interagir com o banco de dados
        listaUsuarios = FXCollections.observableArrayList(); // Cria uma lista observável para os usuários
    }

    @FXML
    void initialize() {
        // Configuração das colunas da tabela para exibir os dados dos usuários
        colunaUsuarioID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUsuarioID())));
        colunaNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

        // Carregar lista de usuários do banco de dados
        try {
            listaUsuarios.addAll(usuarioDAO.buscarTodos()); // Busca todos os usuários e adiciona à lista
        } catch (SQLException e) {
            e.printStackTrace(); // Exibe erro se houver problemas ao buscar usuários
        }

        tabelaUsuarios.setItems(listaUsuarios); // Define a tabela para exibir a lista de usuários
    }

    @FXML
    void btnConfOnAction(ActionEvent event) {
        // Método acionado quando o botão de confirmação é pressionado
        Usuario usuario = new Usuario(); // Cria um novo objeto Usuario
        try {
            usuario.setUsuarioID(usuarioDAO.obterProximoUsuarioID()); // Obtém o próximo ID disponível para o usuário
            usuario.setNome(txtNomeUsuario.getText()); // Define o nome do usuário a partir do campo de texto

            usuarioDAO.inserir(usuario); // Insere o usuário no banco de dados
            listaUsuarios.add(usuario); // Adiciona o novo usuário à lista observável
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuário cadastrado com sucesso!", ButtonType.OK);
            alert.setTitle("Cadastro de Usuário");
            alert.setHeaderText("Informação");
            alert.show(); // Exibe uma mensagem de sucesso
            txtNomeUsuario.clear(); // Limpa o campo de entrada
        } catch (SQLException e) {
            // Trata erro ao tentar cadastrar o usuário
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar usuário!", ButtonType.OK);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro");
            alert.show(); // Exibe uma mensagem de erro
            e.printStackTrace(); // Exibe o erro no console
        }
    }

    @FXML
    void btnVoltarOnAction() {
        // Método acionado para fechar a janela de inclusão de usuário
        Stage stage = (Stage) btnConf.getScene().getWindow(); // Obtém a janela atual
        stage.close(); // Fecha a janela
    }

    @FXML
    void btnExcluirOnAction(ActionEvent event) {
        // Método acionado para excluir um usuário selecionado da tabela
        Usuario usuarioSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem(); // Obtém o usuário selecionado

        if (usuarioSelecionado != null) { // Verifica se algum usuário foi selecionado
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir o usuário " + usuarioSelecionado.getNome() + "?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Atenção!");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) { // Se o usuário confirmar a exclusão
                    try {
                        usuarioDAO.deletar(usuarioSelecionado.getUsuarioID()); // Deleta o usuário do banco de dados
                        listaUsuarios.remove(usuarioSelecionado); // Remove o usuário da lista
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Usuário excluído com sucesso!", ButtonType.OK);
                        infoAlert.setTitle("Exclusão de Usuário");
                        infoAlert.setHeaderText("Informação");
                        infoAlert.show(); // Exibe mensagem de sucesso
                    } catch (SQLException e) {
                        // Trata erro ao tentar excluir o usuário
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir usuário!", ButtonType.OK);
                        errorAlert.setTitle("Erro");
                        errorAlert.setHeaderText("Erro");
                        errorAlert.show(); // Exibe mensagem de erro
                        e.printStackTrace(); // Exibe o erro no console
                    }
                }
            });
        } else {
            // Alerta se nenhum usuário foi selecionado para exclusão
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um usuário para excluir.", ButtonType.OK);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum usuário selecionado");
            alert.show(); // Exibe aviso
        }
    }

    @FXML
    void btnAtualizarOnAction(ActionEvent event) {
        // Método acionado para atualizar os dados de um usuário selecionado
        Usuario usuarioSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem(); // Obtém o usuário selecionado

        if (usuarioSelecionado != null) { // Verifica se um usuário foi selecionado
            try {
                String novoNome = txtNomeUsuario.getText(); // Obtém o novo nome a partir do campo de texto
                if (!novoNome.isEmpty()) { // Verifica se o nome não está vazio
                    usuarioSelecionado.setNome(novoNome); // Atualiza o nome do usuário selecionado
                    usuarioDAO.atualizar(usuarioSelecionado); // Atualiza os dados no banco de dados
                    tabelaUsuarios.refresh(); // Atualiza a tabela para refletir as mudanças
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuário atualizado com sucesso!", ButtonType.OK);
                    alert.setTitle("Atualização de Usuário");
                    alert.setHeaderText("Informação");
                    alert.show(); // Exibe mensagem de sucesso
                    txtNomeUsuario.clear(); // Limpa o campo de entrada
                } else {
                    // Alerta se o nome estiver vazio
                    Alert alert = new Alert(Alert.AlertType.WARNING, "O nome não pode ser vazio!", ButtonType.OK);
                    alert.setTitle("Aviso");
                    alert.setHeaderText("Nome inválido");
                    alert.show(); // Exibe aviso
                }
            } catch (SQLException e) {
                // Trata erro ao tentar atualizar o usuário
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar usuário!", ButtonType.OK);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro");
                alert.show(); // Exibe mensagem de erro
                e.printStackTrace(); // Exibe o erro no console
            }
        } else {
            // Alerta se nenhum usuário foi selecionado para atualização
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um usuário para atualizar.", ButtonType.OK);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum usuário selecionado");
            alert.show(); // Exibe aviso
        }
    }
}