package org.HappyBank.view;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Clase principal para lanzar la aplicación JavaFX para gestionar y mostrar un banco.
 * Inicializa la ventana principal y carga el diseño FXML para la lista de lotes semanales.
 */
public class LoginView extends Application{
    ViewFactory viewFactory;


    /**
     * Punto de entrada para la aplicación JavaFX.
     * Este método lanza la aplicación JavaFX y llama al método start
     *
     * @param args (Argumentos de la línea de comandos)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa la ventana JavaFX y carga el diseño FXML para la sección de inicio de sesión del banco.
     * Este método se llama cuando se lanza la aplicación JavaFX.
     *
     * @param primaryStage (el escenario principal para la aplicación, que representa la ventana principal)
     * @throws IOException (excepción lanzada si ocurre algún error al cargar el archivo FXML o al inicializar la ventana)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        viewFactory = viewFactory.getInstance(primaryStage);
        viewFactory.showLoginView();
    }

}

