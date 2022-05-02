package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AffectedPageController {
    @FXML
    private Label signedInAsLabel;

    public void setSignedInAs(String fullName) {
        signedInAsLabel.setText("Welcome, " + fullName + "!");
    }
}
