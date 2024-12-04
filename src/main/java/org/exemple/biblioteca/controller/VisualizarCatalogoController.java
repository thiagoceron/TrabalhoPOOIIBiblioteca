package org.exemple.biblioteca.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.exemple.biblioteca.dao.LivroDAO;
import org.exemple.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

public class VisualizarCatalogoController {

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

    public VisualizarCatalogoController() {
        livroDAO = new LivroDAO(); // Instância do DAO para operações com o banco de dados
        listaLivros = FXCollections.observableArrayList(); // Inicializa a lista que vai armazenar os livros
    }

    @FXML
    void initialize() {
        // Configurações das colunas para exibir os dados dos livros
        colunaLivroID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLivroID()).asObject());
        colunaTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        colunaAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));

        // Carrega os dados dos livros na tabela ao inicializar
        carregarLivros();
    }

    private void carregarLivros() {
        try {
            // Atualiza a lista de livros a partir da fonte de dados
            atualizarListaLivros();

            // Exibe os livros atualizados na tabela da interface gráfica
            exibirLivrosNaTabela();
        } catch (SQLException e) {
            // Se ocorrer um erro ao carregar os livros, trata o erro
            tratarErroCarregamentoLivros(e);
        }
    }

    private void atualizarListaLivros() throws SQLException {
        // Limpa a lista atual de livros para evitar duplicatas
        listaLivros.clear();

        // Busca todos os livros do banco de dados através do objeto livroDAO
        List<Livro> livros = livroDAO.buscarTodos();

        // Adiciona todos os livros retornados à lista observável
        listaLivros.addAll(livros);
    }

    private void exibirLivrosNaTabela() {
        // Define a lista de livros como a fonte de dados da tabela na interface gráfica
        tabelaLivros.setItems(listaLivros);
    }

    private void tratarErroCarregamentoLivros(SQLException e) {
        // Imprime a stack trace do erro para ajudar na depuração
        e.printStackTrace();

        // Exibe um alerta ao usuário informando que ocorreu um erro ao carregar os livros
        showAlert("Erro", "Não foi possível carregar os livros.");
    }


    private void showAlert(String title, String message) {
        // Exibe uma mensagem de alerta
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleVoltar(ActionEvent event) {
        // Fecha a janela atual ao clicar no botão "Voltar"
        Stage stage = (Stage) tabelaLivros.getScene().getWindow();
        stage.close();
    }
}
