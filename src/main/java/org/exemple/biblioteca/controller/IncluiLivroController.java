package org.exemple.biblioteca.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import org.exemple.biblioteca.dao.LivroDAO;
import org.exemple.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

public class IncluiLivroController {
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private Button btnConf;
    @FXML
    private TableView<Livro> tabelaLivros;
    @FXML
    private TableColumn<Livro, Integer> colunaLivroID;
    @FXML
    private TableColumn<Livro, String> colunaTitulo;
    @FXML
    private TableColumn<Livro, String> colunaAutor;

    private LivroDAO livroDAO;
    private ObservableList<Livro> listaLivros;

        // Construtor
        public IncluiLivroController() {
            livroDAO = new LivroDAO(); // Inicializa o DAO para operações com livros
            listaLivros = FXCollections.observableArrayList(); // Cria uma lista observável para livros
        }

        @FXML
        void initialize() {
            // Define como as colunas da tabela devem mostrar os dados dos livros
            colunaLivroID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLivroID()).asObject());
            colunaTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
            colunaAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));

            carregarLivros(); // Carrega a lista de livros ao inicializar a interface
        }

        private void carregarLivros() {
            try {
                listaLivros.clear(); // Limpa a lista atual de livros
                List<Livro> livros = livroDAO.buscarTodos(); // Busca todos os livros do banco de dados
                listaLivros.addAll(livros); // Adiciona os livros encontrados à lista observável
                tabelaLivros.setItems(listaLivros); // Atualiza a tabela com a nova lista de livros
            } catch (SQLException e) {
                e.printStackTrace(); // Exibe o stack trace do erro
                showAlert("Erro", "Não foi possível carregar os livros."); // Exibe um alerta de erro
            }
        }

        @FXML
        void btnConfOnAction(ActionEvent event) {
            Livro livro = new Livro(); // Cria uma nova instância de Livro
            try {
                // Define as propriedades do livro a partir dos campos de texto
                livro.setLivroID(livroDAO.obterProximoLivroID()); // Obtém o próximo ID disponível
                livro.setTitulo(txtTitulo.getText()); // Define o título
                livro.setAutor(txtAutor.getText()); // Define o autor

                livroDAO.inserir(livro); // Insere o livro no banco de dados
                listaLivros.add(livro); // Adiciona o livro à lista de livros
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Livro cadastrado com sucesso!", ButtonType.OK); // Alerta de sucesso
                alert.setTitle("Cadastro de Livro");
                alert.setHeaderText("Informação");
                alert.show();
                // Limpa os campos de entrada
                txtTitulo.clear();
                txtAutor.clear();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar livro!", ButtonType.OK); // Alerta de erro
                alert.setTitle("Erro");
                alert.setHeaderText("Erro");
                alert.show();
                e.printStackTrace(); // Exibe o stack trace do erro
            }
        }

        @FXML
        void btnVoltarOnAction() {
            Stage stage = (Stage) btnConf.getScene().getWindow(); // Obtém a janela atual
            stage.close(); // Fecha a janela
        }

        @FXML
        void btnExcluirOnAction(ActionEvent event) {
            Livro livroSelecionado = tabelaLivros.getSelectionModel().getSelectedItem(); // Obtém o livro selecionado

            if (livroSelecionado != null) {
                // Exibe um alerta de confirmação antes de excluir
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir o livro " + livroSelecionado.getTitulo() + "?", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Confirmação de Exclusão");
                alert.setHeaderText("Atenção !");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) { // Se o usuário confirmar a exclusão
                        try {
                            livroDAO.deletar(livroSelecionado.getLivroID()); // Remove o livro do banco de dados
                            listaLivros.remove(livroSelecionado); // Remove o livro da lista
                            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Livro excluído com sucesso!", ButtonType.OK); // Alerta de sucesso
                            infoAlert.setTitle("Exclusão de Livro");
                            infoAlert.setHeaderText("Informação");
                            infoAlert.show();
                        } catch (SQLException e) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir livro!", ButtonType.OK); // Alerta de erro
                            errorAlert.setTitle("Erro");
                            errorAlert.setHeaderText("Erro");
                            errorAlert.show();
                            e.printStackTrace(); // Exibe o stack trace do erro
                        }
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um livro para excluir.", ButtonType.OK); // Alerta se nenhum livro estiver selecionado
                alert.setTitle("Aviso");
                alert.setHeaderText("Nenhum livro selecionado");
                alert.show();
            }
        }

        private void showAlert(String title, String message) {
            // Método para exibir alertas de erro
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText("Erro");
            alert.show();
        }

        @FXML
        void btnAtualizarOnAction(ActionEvent event) {
            Livro livroSelecionado = tabelaLivros.getSelectionModel().getSelectedItem(); // Obtém o livro selecionado

            if (livroSelecionado != null) {
                try {
                    // Obtém os novos dados dos campos de texto
                    String novoTitulo = txtTitulo.getText();
                    String novoAutor = txtAutor.getText();

                    if (!novoTitulo.isEmpty() && !novoAutor.isEmpty()) { // Verifica se os campos não estão vazios
                        livroSelecionado.setTitulo(novoTitulo); // Atualiza o título
                        livroSelecionado.setAutor(novoAutor); // Atualiza o autor

                        livroDAO.atualizar(livroSelecionado); // Atualiza o livro no banco de dados
                        tabelaLivros.refresh(); // Atualiza a tabela para refletir as alterações

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Livro atualizado com sucesso!", ButtonType.OK); // Alerta de sucesso
                        alert.setTitle("Atualização de Livro");
                        alert.setHeaderText("Informação");
                        alert.show();

                        // Limpa os campos de entrada
                        txtTitulo.clear();
                        txtAutor.clear();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Título e autor não podem ser vazios!", ButtonType.OK); // Alerta se os campos estiverem vazios
                        alert.setTitle("Aviso");
                        alert.setHeaderText("Dados inválidos");
                        alert.show();
                    }
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar livro!", ButtonType.OK); // Alerta de erro
                    alert.setTitle("Erro");
                    alert.setHeaderText("Erro");
                    alert.show();
                    e.printStackTrace(); // Exibe o stack trace do erro
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um livro para atualizar.", ButtonType.OK); // Alerta se nenhum livro estiver selecionado
                alert.setTitle("Aviso");
                alert.setHeaderText("Nenhum livro selecionado");
                alert.show();
            }
        }
    }
