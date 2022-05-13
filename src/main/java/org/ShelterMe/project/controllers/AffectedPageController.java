package org.ShelterMe.project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.model.AffectedItem;

public class AffectedPageController{
    @FXML
    private Label signedInAsLabel;
    @FXML
    private JFXButton signOutButton;

    public void setSignedInAs(String fullName) {
        signedInAsLabel.setText("Welcome, " + fullName + "!");
    }

    public void handleSignOut(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        GotoLoginController.goToLogin(stage, event);
    }
}
