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
import org.ShelterMe.project.services.VolunteerService;
import org.ShelterMe.project.model.VolunteerItem;

import java.awt.*;
import java.awt.Rectangle;
import java.io.IOException;

public class VolunteerPageController{
    @FXML
    private Label signedInAsLabel;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private JFXButton offersButton;
    @FXML
    private  JFXButton homeButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox offersTab;
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
        homeTab.setManaged(true);
        homeTab.setVisible(true);
        offersTab.setManaged(false);
        offersTab.setVisible(false);
    }
    public void handleOffersPage() {
        homeTab.setManaged(false);
        homeTab.setVisible(false);
        offersTab.setManaged(true);
        offersTab.setVisible(true);
    }

    public void handleAddOffer(javafx.event.ActionEvent event) throws IOException {
        Parent addOffer = FXMLLoader.load(getClass().getClassLoader().getResource("addOfferForm.fxml"));
        Scene scene = new Scene(addOffer);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(offersTab.getScene().getWindow());
        newStage.setTitle("ShelterMe - Add an Offer");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);
    }

    public void handleAddOfferAction(javafx.event.ActionEvent event) throws IOException {

    }
}