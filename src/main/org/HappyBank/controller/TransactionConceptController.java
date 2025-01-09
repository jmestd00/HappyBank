package org.HappyBank.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador para la vista de concepto de transacción.
 * Este controlador se encarga de gestionar la vista de concepto de transacción.
 */
public class TransactionConceptController {
    private String concept;
    @FXML
    private Label conceptLabel;

    /**
     * Establece el concepto de la transacción.
     * @param concept (El concepto de la transacción)
     */
    public void setConcept(String concept) {
        this.concept = concept;
        conceptLabel.setText(concept);
    }

}
