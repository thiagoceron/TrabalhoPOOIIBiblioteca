package org.exemple.biblioteca.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/exemple/biblioteca/view/menus.fxml")); // Carrega o arquivo FXML que define a interface de usuário.
        Parent root = loader.load(); // Carrega o nó raiz (Parent) da cena a partir do arquivo FXML.
        Scene scene = new Scene(root); // Cria a cena principal usando o nó raiz.

        stage.setScene(scene); // Define a cena no palco.
        stage.setTitle("Biblioteca"); // Define o título da janela.
        stage.setResizable(false); // Impede o redimensionamento da janela.
        stage.show(); // Exibe o palco (janela) da aplicação.
    }


    public static void main(String[] args) {
        launch(args);
    }
}
