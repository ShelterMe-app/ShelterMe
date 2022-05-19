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
import org.ShelterMe.project.services.UserService;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Communication;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.CommunicationService;
import org.ShelterMe.project.services.VolunteerService;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.controllers.OfferMenuController;

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
    private JFXButton requestsInboxButton;
    @FXML
    private  JFXButton homeButton;
    @FXML
    private JFXButton requestsButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox offersTab;
    @FXML
    private VBox homeTab;
    @FXML
    private TableView offersTable;

    private Image offerImage;

    @FXML
    private VBox requestsInboxTab;
    @FXML
    private TableView requestsInboxTable;
    @FXML
    private JFXButton showRequestInbox;

    private Image requestInboxImage;

    public void setSignedInAs(Volunteer loggedInVolunteer) {
        this.loggedInVolunteer = loggedInVolunteer;
        signedInAsLabel.setText("Welcome, " + loggedInVolunteer.getFullName() + "!");
        signedInAsLabel11.setText("You currently have: " + loggedInVolunteer.getOffersNo() + " Offers in your Offer list.");
        signedInAsLabel111.setText(loggedInVolunteer.getActiveRequestsNo() + " Requests in your Request inbox.");
        offersTable.setItems(getOffers(loggedInVolunteer.getUsername()));
        requestsInboxTable.setItems(getRequestsInbox(loggedInVolunteer.getUsername()));
        if (this.loggedInVolunteer.isNewOffer() == true) {
            requestsButton.setStyle("-fx-background-color: #44919c;");
            requestsButton.setPrefWidth(115);
            requestsButton.setText("Requests (new)");
            UserService.updateUserInDatabase(loggedInVolunteer);
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

        TableColumn<AffectedItem, String> usernameColumnInbox = new TableColumn<>("Username");
        usernameColumnInbox.setMinWidth(200);
        usernameColumnInbox.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<AffectedItem, String> nameColumnInbox = new TableColumn<>("Name");
        nameColumnInbox.setMinWidth(200);
        nameColumnInbox.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<AffectedItem, String> categoryColumnInbox = new TableColumn<>("Category");
        categoryColumnInbox.setMinWidth(200);
        categoryColumnInbox.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<AffectedItem, String> suppliesColumnInbox = new TableColumn<>("Supplies");
        suppliesColumnInbox.setMinWidth(200);
        suppliesColumnInbox.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<AffectedItem, Float> quantityColumnInbox = new TableColumn<>("Quantity");
        quantityColumnInbox.setMinWidth(200);
        quantityColumnInbox.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<AffectedItem, String> generalInformationColumnInbox = new TableColumn<>("General Information");
        generalInformationColumnInbox.setMinWidth(200);
        generalInformationColumnInbox.setCellValueFactory(new PropertyValueFactory<>("generalInformation"));
        TableColumn<AffectedItem, String> healthConditionColumnInbox = new TableColumn<>("Health Condition");
        healthConditionColumnInbox.setMinWidth(200);
        healthConditionColumnInbox.setCellValueFactory(new PropertyValueFactory<>("healthCondition"));
        requestsInboxTable.getColumns().addAll(usernameColumnInbox, nameColumnInbox, categoryColumnInbox, suppliesColumnInbox, quantityColumnInbox, generalInformationColumnInbox, healthConditionColumnInbox);

    }
    public void handleHomePage() {
        loggedInVolunteer.calculateValues();
        signedInAsLabel11.setText("You currently have: " + loggedInVolunteer.getOffersNo() + " Offers in your Offer list.");
        signedInAsLabel111.setText(loggedInVolunteer.getActiveRequestsNo() + " Requests in your Request inbox.");
        homeTab.setManaged(true);
        homeTab.setVisible(true);
        offersTab.setManaged(false);
        offersTab.setVisible(false);
        requestsInboxTab.setManaged(false);
        requestsInboxTab.setVisible(false);
    }
    public void handleOffersPage() {
        homeTab.setManaged(false);
        homeTab.setVisible(false);
        offersTab.setManaged(true);
        offersTab.setVisible(true);
        requestsInboxTab.setManaged(false);
        requestsInboxTab.setVisible(false);
    }

    public void handleRequestsInboxPage() {
        homeTab.setManaged(false);
        homeTab.setVisible(false);
        offersTab.setManaged(false);
        offersTab.setVisible(false);
        requestsInboxTab.setManaged(true);
        requestsInboxTab.setVisible(true);
    }

    public void handleAddOffer(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addOfferForm.fxml"));
        Parent addOffer = loader.load();
        OfferMenuController newController = loader.getController();
        newController.setLoggedInVolunteer(loggedInVolunteer);
        newController.setOffersTable(offersTable);
        newController.setRemoveCurrentImageStatus(false);
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

    public static ObservableList<VolunteerItem> getOffers(String username) {

        return FXCollections.observableList(VolunteerService.databaseToList(username));
    }

    public static ObservableList<AffectedItem> getRequestsInbox(String username) {
        return FXCollections.observableList(AffectedService.databaseToListInbox(CommunicationService.getSourceIDs(username)));
    }

    public void handleTableClick(MouseEvent event) throws IOException  {
        if (offersTable.getSelectionModel().getSelectedItem() != null)
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
        if (offersTable.getSelectionModel().getSelectedItem() != null) {
            VolunteerItem offer = (VolunteerItem)offersTable.getSelectionModel().getSelectedItem();
            int offerId = offer.getId();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addOfferForm.fxml"));
            Parent editOffer = loader.load();
            OfferMenuController newController = loader.getController();
            newController.setLoggedInVolunteer(loggedInVolunteer);
            newController.setOffersTable(offersTable);
            if (offer.getImageBase64() != null && offer.getImageBase64().length() > 0)
                newController.setRemoveCurrentImageStatus(true);
            else
                newController.setRemoveCurrentImageStatus(false);
            newController.setOfferId(offerId);
            newController.setOfferMenuWelcomeText("Edit Offer");
            newController.setAddOfferButtonText("Edit Offer");
            newController.setOfferNamePlaceholder(offer.getName());
            newController.setOfferCategoryPlaceholder(offer.getCategory());
            newController.setOfferSuppliesPlaceholder(offer.getSupplies());
            newController.setOfferQuantityPlaceholder(Float.toString(offer.getQuantity()));
            newController.setOfferImagePlaceholder("Update image");
            Scene scene = new Scene(editOffer);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(offersTab.getScene().getWindow());
            newStage.setTitle("ShelterMe - Edit an Offer");
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select an offer in order to edit", "Failed to edit offer", 1);
        }
    }

    public void handleRemoveOffer(javafx.event.ActionEvent event) throws IOException {
        if (offersTable.getSelectionModel().getSelectedItem() != null) {
            VolunteerItem offer = (VolunteerItem) offersTable.getSelectionModel().getSelectedItem();
            int offerId = offer.getId();
            VolunteerService.removeItem(offerId);
            JOptionPane.showMessageDialog(null, "Selected offer has been removed", "Successfully removed offer", 1);
            if (offersTable != null)
                offersTable.setItems(VolunteerPageController.getOffers(loggedInVolunteer.getUsername()));
        } else {
            JOptionPane.showMessageDialog(null, "Select an offer in order to remove", "Failed to remove offer", 1);
        }
    }

    public void handleRequestInboxTableClick(MouseEvent event) throws IOException  {
        if (requestsInboxTable.getSelectionModel().getSelectedItem() != null)
            requestInboxImage = VolunteerService.base64ToImage(((AffectedItem)requestsInboxTable.getSelectionModel().getSelectedItem()).getImageBase64());
    }

    public void handleRequestInboxImage(javafx.event.ActionEvent event) throws IOException {
        if (requestInboxImage == null) {
            JOptionPane.showMessageDialog(null, "This request has no image", "Failed to open image", 1);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("requestImageDialog.fxml"));
        Parent imageDialog = loader.load();
        OfferImageDialogController controller = loader.getController();
        controller.setOfferImage(requestInboxImage);
        Scene scene = new Scene(imageDialog);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setTitle("ShelterMe - Request image");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);
    }

    public void handleRequestInbox(javafx.event.ActionEvent event) throws IOException {
        if (requestsInboxTable.getSelectionModel().getSelectedItem() != null)
        {
            AffectedItem connection = (AffectedItem) requestsInboxTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("volunteerReplyForm.fxml"));
            Parent viewRequestInbox = loader.load();
            VolunteerReplyController newController = loader.getController();
            newController.setLoggedInVolunteer(loggedInVolunteer);
            newController.setRequestsInboxTable(requestsInboxTable);
            newController.setRequestId(connection.getId());
            Scene scene = new Scene(viewRequestInbox);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(offersTab.getScene().getWindow());
            newStage.setTitle("ShelterMe - Inbox");
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select a request in order to see it", "Failed to open request", 1);

    public void handleRequestsAction(javafx.event.ActionEvent event) throws IOException {
        if (loggedInVolunteer.isNewOffer() == true) {
            this.loggedInVolunteer.setNewOffer(false);
            requestsButton.setStyle("-fx-background-color: #d6eaed;");
            requestsButton.setPrefWidth(102);
            requestsButton.setText("Requests");
            UserService.updateUserInDatabase(this.loggedInVolunteer);

        }
    }
}