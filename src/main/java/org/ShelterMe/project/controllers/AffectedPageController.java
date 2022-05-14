package org.ShelterMe.project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Rectangle;
import java.io.IOException;

import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.model.AffectedItem;

public class AffectedPageController{
    @FXML
    private Label signedInAsLabel;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private JFXButton requestsButton;
    @FXML
    private  JFXButton homeButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox requestsTab;
    @FXML
    private VBox homeTab;

    public void setSignedInAs(String fullName) {
        signedInAsLabel.setText("Welcome, " + fullName + "!");
    }

    public void handleSignOut(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        GotoLoginController.goToLogin(stage, event);
    }

    public void handleHomePage() {
        homeTab.setVisible(true);
        homeTab.setManaged(true);
        requestsTab.setVisible(false);
        requestsTab.setVisible(false);
    }
    public void handleRequestsPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        requestsTab.setVisible(true);
        requestsTab.setManaged(true);
    }

    public void handleAddRequest(javafx.event.ActionEvent event) throws IOException {
        Parent addRequest = FXMLLoader.load(getClass().getClassLoader().getResource("addRequestForm.fxml"));
        Scene scene = new Scene(addRequest);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(requestsTab.getScene().getWindow());
        newStage.setTitle("ShelterMe - Add a Request");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);
    }

    public void handleAddRequestAction(javafx.event.ActionEvent event) throws IOException {

    }
}
