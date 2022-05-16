package org.ShelterMe.project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.ShelterMe.project.services.VolunteerService;
import org.ShelterMe.project.model.VolunteerItem;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.Rectangle;
import java.io.IOException;

import java.io.FileReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.ShelterMe.project.model.Volunteer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VolunteerPageController{
    private Volunteer loggedInVolunteer;
    @FXML
    private Label signedInAsLabel;
    @FXML
    private Label signedInAsLabel1;
    @FXML
    private Label signedInAsLabel11;
    @FXML
    private Label signedInAsLabel111;
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
    @FXML
    private TableView offersTable;

    private Image offerImage;

    public void setSignedInAs(Volunteer loggedInVolunteer) {
        this.loggedInVolunteer = loggedInVolunteer;
        signedInAsLabel.setText("Welcome, " + loggedInVolunteer.getFullName() + "!");
        signedInAsLabel11.setText("You currently have: " + loggedInVolunteer.getOffersNo() + " Offers in your Requests list.");
        offersTable.setItems(getOffers(loggedInVolunteer.getUsername()));
    }
    public void handleSignOut(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        GotoLoginController.goToLogin(stage, event);
    }
    @FXML
    public void initialize() {
        TableColumn<VolunteerItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(187);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<VolunteerItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(187);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<VolunteerItem, String> suppliesColumn = new TableColumn<>("Supplies");
        suppliesColumn.setMinWidth(187);
        suppliesColumn.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<Volunteer, Float> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(187);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        offersTable.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn);
    }
    public void handleHomePage() {
        loggedInVolunteer.calculateValues();
        signedInAsLabel11.setText("You currently have: " + loggedInVolunteer.getOffersNo() + " Offers in your Requests list.");
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
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addOfferForm.fxml"));
        Parent addOffer = loader.load();
        OfferMenuController newController = loader.getController();
        newController.setLoggedInVolunteer(loggedInVolunteer);
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

    public ObservableList<VolunteerItem> getOffers(String username) {

        return FXCollections.observableList(VolunteerService.databaseToList(username));
    }

    public void handleTableClick(MouseEvent event) throws IOException  {
        offerImage = VolunteerService.base64ToImage(((VolunteerItem)offersTable.getSelectionModel().getSelectedItem()).getImageBase64());
    }

    public void handleOfferImage(javafx.event.ActionEvent event) throws IOException {
        if (offerImage == null) {
            JOptionPane.showMessageDialog(null, "This offer has no image", "Failed to open image", 1);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("offerImageDialog.fxml"));
        Parent imageDialog = loader.load();
        OfferImageDialogController controller = loader.getController();
        controller.setOfferImage(offerImage);
        Scene scene = new Scene(imageDialog);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setTitle("ShelterMe - Offer image");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);
    }

    public void handleEditOffer(javafx.event.ActionEvent event) throws IOException {
    }
}