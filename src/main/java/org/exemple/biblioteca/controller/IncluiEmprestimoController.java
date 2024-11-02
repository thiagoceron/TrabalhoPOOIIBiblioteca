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
import org.exemple.biblioteca.dao.EmprestimoDAO;
import org.exemple.biblioteca.dao.UsuarioDAO;
import org.exemple.biblioteca.model.Emprestimo;
import org.exemple.biblioteca.model.Livro;
import org.exemple.biblioteca.model.Usuario;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.sql.SQLException;
import java.time.LocalDate;


public class IncluiEmprestimoController {
    @FXML
    private Button btnConf;
    @FXML
    private TableView<Emprestimo> tabelaEmprestimos;
    @FXML
    private TableColumn<Emprestimo, String> colunaUsuarioID;
    @FXML
    private TableColumn<Emprestimo, String> colunaUsuario;
    @FXML
    private TableColumn<Emprestimo, String> colunaLivroID;
    @FXML
    private TableColumn<Emprestimo, String> colunaLivro;
    @FXML
    private TableColumn<Emprestimo, String> colunaDataEmprestimo;
    @FXML
    private TableColumn<Emprestimo, String> colunaDataDevolucao;

    @FXML
    private TextField txtUsuarioID;
    @FXML
    private TextField txtLivroID;
    @FXML
    private TextField txtDataEmprestimo;
    @FXML
    private TextField txtDataDevolucao;

    private EmprestimoDAO emprestimoDAO;
    private ObservableList<Emprestimo> listaEmprestimos;
    private UsuarioDAO usuarioDAO;

    public IncluiEmprestimoController() {
        // Inicializa os DAOs e a lista de empréstimos
        emprestimoDAO = new EmprestimoDAO();
        usuarioDAO = new UsuarioDAO();
        listaEmprestimos = FXCollections.observableArrayList(); // Cria uma lista observável para os empréstimos
    }

    @FXML
    void initialize() {
        // Configura as colunas da tabela com os dados dos empréstimos
        colunaUsuarioID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUsuario().getUsuarioID())));
        colunaUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsuario().getNome()));
        colunaLivroID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLivro().getLivroID())));
        colunaLivro.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLivro().getTitulo()));
        colunaDataEmprestimo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataEmprestimo().toString()));
        colunaDataDevolucao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataDevolucao() != null ? cellData.getValue().getDataDevolucao().toString() : ""));

        // Define a lista de empréstimos como a fonte de dados da tabela
        tabelaEmprestimos.setItems(listaEmprestimos);

        // Carrega os empréstimos existentes na tabela
        carregarEmprestimos();
    }

    @FXML
    void btnConfOnAction(ActionEvent event) {
        // Chama o método para verificar o usuário e inserir um novo empréstimo
        verificarUsuarioEInserirEmprestimo(event);
    }

    private void showAlert(String title, String message) {
        // Método para mostrar uma janela de alerta com uma mensagem de erro
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void carregarEmprestimos() {
        // Método para carregar todos os empréstimos do banco de dados
        try {
            listaEmprestimos.clear(); // Limpa a lista atual
            listaEmprestimos.addAll(emprestimoDAO.buscarTodos()); // Adiciona todos os empréstimos à lista
            tabelaEmprestimos.setItems(listaEmprestimos); // Atualiza a tabela com a nova lista
        } catch (SQLException e) {
            e.printStackTrace(); // Trata exceções de SQL
        }
    }

    @FXML
    void btnVoltarOnAction() {
        // Fecha a janela atual
        Stage stage = (Stage) btnConf.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnExcluirOnAction(ActionEvent event) {
        // Obtém o empréstimo selecionado na tabela
        Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();

        // Verifica se um empréstimo foi selecionado
        if (emprestimoSelecionado != null) {
            // Cria um alerta de confirmação para exclusão
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir o empréstimo do livro " + emprestimoSelecionado.getLivro().getTitulo() + " para o usuário " + emprestimoSelecionado.getUsuario().getNome() + "?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Atenção!");

            // Aguarda a resposta do usuário
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // Se o usuário confirmar, tenta excluir o empréstimo
                    try {
                        emprestimoDAO.deletar(emprestimoSelecionado.getEmprestimoID()); // Exclui do banco de dados
                        listaEmprestimos.remove(emprestimoSelecionado); // Remove da lista
                        // Mostra um alerta de sucesso
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Empréstimo excluído com sucesso!", ButtonType.OK);
                        infoAlert.setTitle("Exclusão de Empréstimo");
                        infoAlert.setHeaderText("Informação");
                        infoAlert.show();
                    } catch (SQLException e) {
                        // Mostra um alerta de erro se a exclusão falhar
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir empréstimo!", ButtonType.OK);
                        errorAlert.setTitle("Erro");
                        errorAlert.setHeaderText(" Erro");
                        errorAlert.show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            // Se nenhum empréstimo foi selecionado, mostra um alerta
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um empréstimo para excluir.", ButtonType.OK);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum empréstimo selecionado");
            alert.show();
        }
    }

    @FXML
    void verificarUsuarioEInserirEmprestimo(ActionEvent event) {
        // Cria um novo empréstimo
        Emprestimo emprestimo = new Emprestimo();

        // Define o formato de data esperado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Obtém o usuário a partir do ID informado
            Usuario usuario = new Usuario();
            usuario.setUsuarioID(Integer.parseInt(txtUsuarioID.getText()));

            // Verifica se o usuário existe
            if (usuarioDAO.usuarioExiste(usuario.getUsuarioID())) {
                emprestimo.setUsuario(usuario); // Define o usuário do empréstimo
            } else {
                // Se o usuário não existe, mostra um alerta
                showAlert("Erro", "Usuário não encontrado.");
                return;
            }

            // Obtém o livro a partir do ID informado
            Livro livro = new Livro();
            livro.setLivroID(Integer.parseInt(txtLivroID.getText()));
            emprestimo.setLivro(livro); // Define o livro do empréstimo

            // Tenta definir a data de empréstimo
            try {
                emprestimo.setDataEmprestimo(LocalDate.parse(txtDataEmprestimo.getText(), formatter));
            } catch (DateTimeParseException e) {
                // Se a data estiver no formato incorreto, mostra um alerta
                showAlert("Erro", "Data de Empréstimo deve estar no formato YYYY-MM-DD.");
                return;
            }

            // Tenta definir a data de devolução (se informada)
            if (!txtDataDevolucao.getText().isEmpty()) {
                try {
                    emprestimo.setDataDevolucao(LocalDate.parse(txtDataDevolucao.getText(), formatter));
                } catch (DateTimeParseException e) {
                    // Se a data estiver no formato incorreto, mostra um alerta
                    showAlert("Erro", "Data de Devolução deve estar no formato YYYY-MM-DD.");
                    return;
                }
            } else {
                emprestimo.setDataDevolucao(null); // Se não informada, define como null
            }

            // Insere o empréstimo no banco de dados
            emprestimoDAO.inserir(emprestimo);

            // Adiciona o empréstimo à lista e atualiza a tabela
            listaEmprestimos.add(emprestimo);
            tabelaEmprestimos.refresh();

            // Limpa os campos de texto
            txtUsuarioID.clear();
            txtLivroID.clear();
            txtDataEmprestimo.clear();
            txtDataDevolucao.clear();

        } catch (NumberFormatException e) {
            // Se houver um erro de formato nos campos, mostra um alerta
            showAlert("Erro", "Por favor, digite os dados corretamente.");
        } catch (SQLException e) {
            e.printStackTrace(); // Trata exceções de SQL
        }
    }
}