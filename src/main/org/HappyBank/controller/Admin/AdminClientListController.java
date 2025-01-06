package org.HappyBank.controller.Admin;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Properties;

import static org.HappyBank.model.DatabaseManager.*;

/**
 * Controller for the client list view.
 */
public class AdminClientListController {
    private ViewFactory viewFactory = ViewFactory.getInstance(null);
    private Administrator administrator;
    @FXML
    private Label welcomeLabel;
    private String username;
    private boolean backUp = false;

    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> NIFCol;
    @FXML
    private TableColumn<Client, String> NameCol;
    @FXML
    private TableColumn<Client, String> accountCol;
    @FXML
    private TableColumn<Client, String> creditCardCol;
    @FXML
    private TableColumn<Client, Void> editCol;
    @FXML
    private TableColumn<Client, Void> transactionHistoryCol;
    @FXML
    private TextField searchBar;
    @FXML
    private Rectangle slider;
    @FXML
    private Rectangle background;
    @FXML
    private Text off;
    @FXML
    private Text on;
    @FXML
    private Pagination pagination;
    private final int ROWS_PER_PAGE = 7;

    private ArrayList<Client> fullListClients = new ArrayList<>();
    private ArrayList<Client> filteredList = new ArrayList<>();

    /**
     * Initializes the bbdd instance, read the options.config, setup the list data and initializes the pagination.
     */
    public void initialize(){
        try {
            getInstance();
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        readConfig();
        searchBar.setContextMenu(new ContextMenu());
        setupData();
        clientTable.setSelectionModel(null);
        NIFCol.setReorderable(false);
        NameCol.setReorderable(false);
        accountCol.setReorderable(false);
        creditCardCol.setReorderable(false);
        editCol.setReorderable(false);
        transactionHistoryCol.setReorderable(false);
        NIFCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return data.getValue().getProperties()[0];
        }
        });
        NameCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return data.getValue().getProperties()[1];
        }
        });
        accountCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return data.getValue().getProperties()[2];
        }
        });
        creditCardCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return data.getValue().getProperties()[3];
        }
        });
        editCol.setCellFactory(param -> new TableCell<Client, Void>() {
            private final Button editButton = new Button("");
            {
                editButton.setOnAction(event -> {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        viewFactory.showAdminClientEditView(selectedClient, administrator);
                    }
                });

                editButton.getStylesheets().add(getClass().getResource("/css/admin/AdminClientList.css").toString());
                editButton.getStyleClass().add("editButton");
                editButton.setPrefHeight(53);
                editButton.setPrefWidth(53);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        setGraphic(editButton);  // Mostrar el botón si el Cliente es válido
                        setAlignment(Pos.CENTER);  // Alinear el botón
                    } else {
                        setGraphic(null);  // No mostrar el botón si el Cliente es nulo
                    }
                }
            }
        });
        transactionHistoryCol.setCellFactory(param -> new TableCell<Client, Void>() {
            private final Button transactionHistoryButton = new Button("");
            {
                transactionHistoryButton.setOnAction(event -> {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        viewFactory.showAdminTransactionHistoryView(selectedClient, administrator);
                    }
                });

                transactionHistoryButton.getStylesheets().add(getClass().getResource("/css/admin/AdminClientList.css").toString());
                transactionHistoryButton.getStyleClass().add("transactionHistoryButton");
                transactionHistoryButton.setPrefHeight(53);
                transactionHistoryButton.setPrefWidth(53);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                // Verificar si la fila está vacía o si el Batch es nulo
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);  // No mostrar el botón si está vacío
                } else {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        setGraphic(transactionHistoryButton);  // Mostrar el botón si el Batch es válido
                        setAlignment(Pos.CENTER);  // Alinear el botón
                    } else {
                        setGraphic(null);  // No mostrar el botón si el Batch es nulo
                    }
                }
            }
        });
        int totalPage = (int) (Math.ceil(fullListClients.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            changeTableView(newValue.intValue(), ROWS_PER_PAGE);
        });
    }

    /**
     * Changes the table view to show the clients of the current page.
     * @param index
     * @param limit
     */
    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, fullListClients.size());
        int minIndex = Math.min(toIndex, fullListClients.size());

        ObservableList<Client> pageData = FXCollections.observableArrayList(fullListClients.subList(fromIndex, minIndex));

        int remainingRows = limit - pageData.size();
        for (int i = 0; i < remainingRows; i++) {
            pageData.add(null);
        }

        SortedList<Client> sortedData = new SortedList<>(pageData);
        sortedData.comparatorProperty().bind(clientTable.comparatorProperty());

        clientTable.setItems(sortedData);
    }

    /**
     * Refreshes the table view.
     */
    public void refreshTable() {
        int totalPage = (int) Math.ceil(fullListClients.size() * 1.0 / ROWS_PER_PAGE);
        pagination.setPageCount(totalPage);

        changeTableView(pagination.getCurrentPageIndex(), ROWS_PER_PAGE);
    }

    /**
     * Sets up the data of the clients.
     */
    private void setupData() {
       try {
        fullListClients = getAllClients();
       } catch (HappyBankException e) {
           e.printStackTrace();
       }
    }

    /**
     * Method that closes the current session.
     */
    public void closeSession() {
        viewFactory.showLoginView();
    }

    /**
     * Method that goes back to the main window.
     */
    public void goBackToMain() {
        viewFactory.showAdminMainWindow(administrator.getNIF());
    }

    /**
     * Method that shows the legend.
     */
    public void showLegend() {
        viewFactory.showAdminLegend();
    }

    /**
     * Method that shows the add client view.
     */
    public void showAdd() {
        viewFactory.showAddClient(administrator);
    }

    /**
     * Method that searches a client.
     */
    public void search() {
        String filter = searchBar.getText().toLowerCase();
        if (!searchBar.getText().equals("")) {
        fullListClients.clear();
        try {
            fullListClients = DatabaseManager.searchClient(filter, filter, filter);
            refreshTable();
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        } else {
        fullListClients.clear();
            try {
            fullListClients = getAllClients();
            } catch (HappyBankException e) {
                e.printStackTrace();
            }
        }
        refreshTable();
    }

    /**
     * Method that reads the configuration file.
     */
    public void readConfig() {
        Properties prop = new Properties();
        try {
            File file = new File("options.config");
            if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();
            backUp = Boolean.parseBoolean(prop.getProperty("bbddBackup"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (backUp) {
            slider.setTranslateX(63);
            background.setStyle("-fx-fill: lightgreen;");
            off.setVisible(false);
            on.setVisible(true);
        } else {
            slider.setTranslateX(0);
            background.setStyle("-fx-fill: red;");
            off.setVisible(true);
            on.setVisible(false);
        }
    }

    /**
     * Method that writes the configuration file.
     * @param backUp
     */
    private void writeConfig(boolean backUp) {
        String resourceName = "options.config";
        File externalFile = new File("options.config");

        // Copiar el archivo desde el classpath al sistema de archivos si no existe
        if (!externalFile.exists()) {
            try (InputStream input = AdminClientListController.class.getClassLoader().getResourceAsStream(resourceName)) {
                if (input == null) {
                    return;
                }

                Files.copy(input, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Escribir en el archivo copiado
        try (FileWriter writer = new FileWriter(externalFile, false)) { // 'true' para agregar contenido
            writer.write("bbddBackup=" + backUp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that activates the diary backUp.
     */
    public void bbddBackUp() {
        backUp = !backUp;
        writeConfig(backUp);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), slider);
        if (backUp) {
            transition.setToX(63); // Mover a la derecha
            background.setStyle("-fx-fill: lightgreen;");
            off.setVisible(false);
            on.setVisible(true);
        } else {
            transition.setToX(0); // Mover a la izquierda
            background.setStyle("-fx-fill: red;");
            off.setVisible(true);
            on.setVisible(false);
        }
        transition.play();
    }

    /**
     * Method that sets the administrator data.
     * @param admin
     */
    public void setAdmin(Administrator admin) {
        this.administrator = admin;
        username = admin.getName() + " " + admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
    }

}
