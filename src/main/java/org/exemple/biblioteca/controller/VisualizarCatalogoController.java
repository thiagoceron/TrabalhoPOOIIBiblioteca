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
            atualizarListaLivros();
            exibirLivrosNaTabela();
        } catch (SQLException e) {
            tratarErroCarregamentoLivros(e);
        }
    }

    private void atualizarListaLivros() throws SQLException {
        listaLivros.clear(); // Limpa a lista antes de adicionar novos dados
        List<Livro> livros = livroDAO.buscarTodos(); // Busca todos os livros do banco
        listaLivros.addAll(livros); // Adiciona os livros na lista observável
    }

    private void exibirLivrosNaTabela() {
        tabelaLivros.setItems(listaLivros); // Exibe a lista na tabela
    }

    private void tratarErroCarregamentoLivros(SQLException e) {
        e.printStackTrace(); // Loga o erro para depuração
        showAlert("Erro", "Não foi possível carregar os livros."); // Alerta em caso de erro
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
