package org.HappyBank.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.HappyBank.controller.LoginController;

import java.util.Objects;

public class LoginView extends Application{
    Image happyBankLogo = new Image(String.valueOf(getClass().getResource("/images/HappyBank512_512.png")));
    LoginController loginController = new LoginController();

/**
 * Main class to launch the JavaFX application for managing and displaying a list of batches.
 * It initializes the primary window and loads the FXML layout for the weekly batches list.
 */

    /**
     * Entry point for the JavaFX application.
     * This method launches the JavaFX application and calls the start method
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the JavaFX window and loads the FXML layout for the weekly batches list.
     * This method is called when the JavaFX application is launched
     *
     * @param primaryStage the primary stage for the application, representing the main window
     * @throws Exception exception thrown if any error occurs while loading the FXML file or initializing the window
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/loginView.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

