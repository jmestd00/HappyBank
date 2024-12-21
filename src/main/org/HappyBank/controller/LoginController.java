package org.HappyBank.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox accountPicker;
    private String username;
    private String password;
    private String accountType;
    private Stage mainStage;

    private void getFields() {
        username = userField.getText();
        password = passwordField.getText();
        accountType = accountPicker.getValue().toString();
        mainStage = (Stage) userField.getScene().getWindow();
    }

    @FXML
    public void loginUser() {
        getFields();
        if (accountType.equals("ADMINISTRADOR")) {
            if (tryAdminLogin()) {
                showWindow(new FXMLLoader(getClass().getResource("/fxml/Admin/adminMainWindow.fxml")));
            } else {
                showError(new FXMLLoader(getClass().getResource("/fxml/Error/loginError.fxml")));
            }
        } else {
            if (tryClientLogin()) {
                showWindow(new FXMLLoader(getClass().getResource("/fxml/Client/clientMainWindow.fxml")));
            } else {
                showError(new FXMLLoader(getClass().getResource("/fxml/Error/loginError.fxml")));
            }
        }
    }

    private boolean tryAdminLogin() {
        boolean success = false;
        if (username.toLowerCase().equals("admin") && password.equals("Root1234")) {
            success = true;
        }
        return success;
    }

    private boolean tryClientLogin() {
        boolean success = false;
        if (username.toLowerCase().equals("viiceetee") && password.equals("Root1234")) {
            success = true;
        }
        return success;
    }

    private void showWindow(FXMLLoader fxmlLoader) {
        // Show window
    }

    private void showError(FXMLLoader fxmlLoader) {
        // Show error
        try {
            // Cargar el archivo FXML del popup
            Parent popupRoot = fxmlLoader.load();
            Stage popupStage = new Stage();
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setTitle("ERROR");
            //popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal
            popupStage.setScene(new Scene(popupRoot));
            popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/warn_icon.png")));
            popupStage.setOnShown(event -> {
                // Obtener dimensiones de la ventana principal o pantalla
                double centerX = userField.getScene().getWindow().getX() + userField.getScene().getWindow().getWidth() / 2;
                double centerY = userField.getScene().getWindow().getY() + userField.getScene().getWindow().getHeight() / 2;
                // Calcular posición para centrar el popup
                popupStage.setX(centerX - popupStage.getWidth() / 2);
                popupStage.setY(centerY - popupStage.getHeight() / 2);
            });
            popupStage.show();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), // Duración antes de ejecutar la acción
                    event -> popupStage.close() // Acción para cerrar la ventana
            ));
            timeline.setCycleCount(1); // Ejecutar solo una vez
            timeline.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
