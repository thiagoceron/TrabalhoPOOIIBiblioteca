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
import org.exemple.biblioteca.dao.AvaliacaoDAO;
import org.exemple.biblioteca.model.Avaliacao;

import java.sql.SQLException;
import java.util.List;

public class AvaliacoesController {
    @FXML
    private TextField txtNomeUsuario;
    @FXML
    private TextField txtLivroTitulo;
    @FXML
    private TextField txtTexto;
    @FXML
    private Button btnConf;
    @FXML
    private TableView<Avaliacao> tabelaAvaliacoes;
    @FXML
    private TableColumn<Avaliacao, String> colunaNomeUsuario;
    @FXML
    private TableColumn<Avaliacao, String> colunaLivroTitulo;
    @FXML
    private TableColumn<Avaliacao, String> colunaTexto;

    private AvaliacaoDAO avaliacaoDAO;
    private ObservableList<Avaliacao> listaAvaliacoes;

    public AvaliacoesController() {
        // Inicializa o DAO e a lista de avaliações
        avaliacaoDAO = new AvaliacaoDAO();
        listaAvaliacoes = FXCollections.observableArrayList(); // Cria uma lista observável para as avaliações
    }

    @FXML
    void initialize() {
        // Define as fábricas de células para as colunas da tabela
        colunaNomeUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomeUsuario()));
        colunaLivroTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLivroTitulo()));
        colunaTexto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTexto()));

        // Carrega as avaliações existentes na tabela
        carregarAvaliacoes();
    }

    private void carregarAvaliacoes() {
        // Método para carregar todas as avaliações do banco de dados
        try {
            listaAvaliacoes.clear(); // Limpa a lista atual
            List<Avaliacao> avaliacoes = avaliacaoDAO.buscarTodos(); // Busca todas as avaliações
            listaAvaliacoes.addAll(avaliacoes); // Adiciona as avaliações à lista
            tabelaAvaliacoes.setItems(listaAvaliacoes); // Atualiza a tabela com a nova lista
        } catch (SQLException e) {
            e.printStackTrace(); // Trata exceções de SQL
            showAlert("Erro", "Não foi possível carregar as avaliações."); // Mostra um alerta em caso de erro
        }
    }

    @FXML
    void btnConfOnAction(ActionEvent event) {
        // Método chamado ao clicar no botão de confirmação
        Avaliacao avaliacao = new Avaliacao(); // Cria uma nova avaliação
        try {
            // Define os dados da avaliação
            avaliacao.setAvaliacaoID(avaliacaoDAO.obterProximoAvaliacaoID()); // Obtém o próximo ID de avaliação
            avaliacao.setLivroTitulo(txtLivroTitulo.getText()); // Define o título do livro
            avaliacao.setTexto(txtTexto.getText()); // Define o texto da avaliação
            avaliacao.setNomeUsuario(txtNomeUsuario.getText()); // Define o nome do usuário
            avaliacaoDAO.inserir(avaliacao); // Insere a avaliação no banco de dados
            listaAvaliacoes.add(avaliacao); // Adiciona a avaliação à lista
            // Mostra um alerta de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avaliação cadastrada com sucesso!", ButtonType.OK);
            alert.setTitle("Cadastro de Avaliação");
            alert.setHeaderText("Informação");
            alert.show();
            // Limpa os campos de texto
            txtLivroTitulo.clear();
            txtTexto.clear();
            txtNomeUsuario.clear();
        } catch (SQLException e) {
            // Mostra um alerta de erro se a inserção falhar
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar avaliação!", ButtonType.OK);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro");
            alert.show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnExcluirOnAction(ActionEvent event) {
        // Obtém a avaliação selecionada na tabela
        Avaliacao avaliacaoSelecionada = tabelaAvaliacoes.getSelectionModel().getSelectedItem();

        // Verifica se uma avaliação foi selecionada
        if (avaliacaoSelecionada != null) {
            // Cria um alerta de confirmação para exclusão
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir a avaliação do livro " + avaliacaoSelecionada.getLivroTitulo() + "?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Atenção !");

            // Aguarda a resposta do usuário
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // Se o usuário confirmar, tenta excluir a avaliação
                    try {
                        avaliacaoDAO.deletar(avaliacaoSelecionada.getAvaliacaoID()); // Exclui do banco de dados
                        listaAvaliacoes.remove(avaliacaoSelecionada); // Remove da lista
                        // Mostra um alerta de sucesso
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Avaliação excluída com sucesso!", ButtonType.OK);
                        infoAlert.setTitle("Exclusão de Avaliação");
                        infoAlert.setHeaderText("Informação");
                        infoAlert.show();
                    } catch (SQLException e) {
                        // Mostra um alerta de erro se a exclusão falhar
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir avaliação!", ButtonType.OK);
                        errorAlert.setTitle("Erro");
                        errorAlert.setHeaderText("Erro");
                        errorAlert.show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            // Se nenhuma avaliação foi selecionada, mostra um alerta
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione uma avaliação para excluir.", ButtonType.OK);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhuma avaliação selecionada");
            alert.show();
        }
    }

    @FXML
    void btnAtualizarOnAction(ActionEvent event) {
        // Obtém a avaliação selecionada na tabela
        Avaliacao avaliacaoSelecionada = tabelaAvaliacoes.getSelectionModel().getSelectedItem();

        // Verifica se uma avaliação foi selecionada
        if (avaliacaoSelecionada != null) {
            // Tenta atualizar a avaliação
            try {
                String novoLivroTitulo = txtLivroTitulo.getText();
                String novoTexto = txtTexto.getText();
                String novoNomeUsuario = txtNomeUsuario.getText();
                if (!novoLivroTitulo.isEmpty() && !novoTexto.isEmpty() && !novoNomeUsuario.isEmpty()) {
                    avaliacaoSelecionada.setLivroTitulo(novoLivroTitulo);
                    avaliacaoSelecionada.setTexto(novoTexto);
                    avaliacaoSelecionada.setNomeUsuario(novoNomeUsuario);
                    avaliacaoDAO.atualizar(avaliacaoSelecionada); // Atualiza no banco de dados
                    tabelaAvaliacoes.refresh(); // Atualiza a tabela
                    // Mostra um alerta de sucesso
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avaliação atualizada com sucesso!", ButtonType.OK);
                    alert.setTitle("Atualização de Avaliação");
                    alert.setHeaderText("Informação");
                    alert.show();
                    // Limpa os campos de texto
                    txtLivroTitulo.clear();
                    txtTexto.clear();
                    txtNomeUsuario.clear();
                } else {
                    // Se os campos estiverem vazios, mostra um alerta
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Título, texto e nome do usuário não podem ser vazios!", ButtonType.OK);
                    alert.setTitle("Aviso");
                    alert.setHeaderText("Dados inválidos");
                    alert.show();
                }
            } catch (SQLException e) {
                // Mostra um alerta de erro se a atualização falhar
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar avaliação!", ButtonType.OK);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro");
                alert.show();
                e.printStackTrace();
            }
        } else {
            // Se nenhuma avaliação foi selecionada, mostra um alerta
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione uma avaliação para atualizar.", ButtonType.OK);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhuma avaliação selecionada");
            alert.show();
        }
    }

    private void showAlert(String title, String message) {
        createAlert(Alert.AlertType.ERROR, title, "Erro", message).show();
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }


    @FXML
    void btnVoltarOnAction(ActionEvent event) {
        // Fecha a janela atual
        Stage stage = (Stage) btnConf.getScene().getWindow();
        stage.close();
    }
}



