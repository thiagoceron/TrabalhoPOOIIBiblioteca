package org.exemple.biblioteca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MenusController {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        // Método chamado automaticamente ao inicializar o controller
        // Carrega uma imagem de fundo para a interface principal
        Image image = new Image(getClass().getResourceAsStream("/org/exemple/biblioteca/image/biblioteca.jpg"));
        if (image != null) {
            imageView.setImage(image); // Define a imagem de fundo se for encontrada
        } else {
            System.out.println("Imagem não encontrada!"); // Log para depuração se a imagem não for encontrada
        }
    }

    @FXML
    void incluiUsuarioOnAction(ActionEvent event) {
        // Método acionado ao clicar no item de menu para incluir um usuário
        try {
            // Carrega a cena de inclusão de usuário
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/exemple/biblioteca/view/IncluiUsuario.fxml"));
            Parent root = loader.load(); // Carrega o layout FXML correspondente
            Stage newStage = new Stage(); // Cria uma nova janela
            Scene newScene = new Scene(root); // Cria a cena a partir do layout carregado
            newStage.setScene(newScene); // Define a cena da nova janela
            newStage.setTitle("Incluir Usuário"); // Define o título da nova janela
            newStage.setResizable(false); // Define a janela como não redimensionável
            newStage.show(); // Exibe a nova janela
        } catch (IOException e) {
            e.printStackTrace(); // Exibe o erro se a janela não puder ser carregada
        }
    }

    @FXML
    void incluiLivroOnAction(ActionEvent event) {
        // Método acionado ao clicar no item de menu para incluir um livro
        abrirJanela("/org/exemple/biblioteca/view/IncluiLivro.fxml", "Incluir Livro");
    }
    private void abrirJanela(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(titulo);
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException e) {
            // Loga o erro com mais contexto
            System.err.println("Erro ao abrir a janela: " + titulo);
            e.printStackTrace();
        }
    }


    @FXML
    void incluiEmprestimoOnAction(ActionEvent event) {
        // Método acionado ao clicar no item de menu para incluir um empréstimo
        try {
            // Carrega a cena de inclusão de empréstimo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/exemple/biblioteca/view/IncluiEmprestimo.fxml"));
            Parent root = loader.load(); // Carrega o layout FXML correspondente
            Stage newStage = new Stage(); // Cria uma nova janela
            Scene newScene = new Scene(root); // Cria a cena a partir do layout carregado
            newStage.setScene(newScene); // Define a cena da nova janela
            newStage.setTitle("Incluir Empréstimo"); // Define o título da nova janela
            newStage.setResizable(false); // Define a janela como não redimensionável
            newStage.show(); // Exibe a nova janela
        } catch (IOException e) {
            e.printStackTrace(); // Exibe o erro se a janela não puder ser carregada
        }
    }

    @FXML
    void menuCatalogoOnAction(ActionEvent event) {
        // Método acionado ao clicar no item de menu para visualizar o catálogo de livros
        try {
            // Carrega a cena do catálogo de livros
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/exemple/biblioteca/view/VisualizarCatalogo.fxml"));
            Parent root = loader.load(); // Carrega o layout FXML correspondente
            Stage newStage = new Stage(); // Cria uma nova janela
            Scene newScene = new Scene(root); // Cria a cena a partir do layout carregado
            newStage.setScene(newScene); // Define a cena da nova janela
            newStage.setTitle("Catálogo de Livros"); // Define o título da nova janela
            newStage.setResizable(false); // Define a janela como não redimensionável
            newStage.show(); // Exibe a nova janela
        } catch (IOException e) {
            e.printStackTrace(); // Exibe o erro se a janela não puder ser carregada
        }
    }

    @FXML
    void menuAvaliacoesOnAction() {
        // Método acionado ao clicar no item de menu para visualizar avaliações
        try {
            // Carrega a cena de avaliações
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/exemple/biblioteca/view/Avaliacoes.fxml"));
            Scene scene = new Scene(loader.load()); // Cria a cena a partir do layout carregado
            Stage stage = new Stage(); // Cria uma nova janela
            stage.setScene(scene); // Define a cena da nova janela
            stage.setTitle("Avaliações"); // Define o título da nova janela
            stage.show(); // Exibe a nova janela
        } catch (Exception e) {
            e.printStackTrace(); // Exibe o erro se a janela não puder ser carregada
        }
    }
}
