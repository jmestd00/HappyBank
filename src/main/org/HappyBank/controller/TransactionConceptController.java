package org.HappyBank.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class TransactionConceptController {
    private String concept;
    @FXML
    private Label conceptLabel;

    public void setConcept(String concept) {
        this.concept = concept;
        conceptLabel.setText(concept);
    }

}
