package org.ShelterMe.project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ShelterMe.project.model.*;
import org.ShelterMe.project.services.UserService;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.CommunicationService;
import org.ShelterMe.project.services.VolunteerService;

import javafx.util.Callback;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

import javax.swing.JOptionPane;

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
    private JFXButton historyButton;
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
    private VBox affectedTab;
    @FXML
    private VBox historyTab;
    @FXML
    private TableView offersTable;
    @FXML
    private TableView affectedTable;

    private Image offerImage;

    @FXML
    private VBox requestsInboxTab;
    @FXML
    private TableView requestsInboxTable;
    @FXML
    private JFXButton showRequestInbox;

    private Image requestInboxImage;
    @FXML
    private TableView historyTable;
    @FXML
    private JFXButton viewAffectedInfo;
    private Image historyImage;

    public void setSignedInAs(Volunteer loggedInVolunteer) {
        this.loggedInVolunteer = loggedInVolunteer;
        offersTable.setItems(getOffers(loggedInVolunteer.getUsername()));
        affectedTable.setItems(getAffected(loggedInVolunteer.getCountry()));
        requestsInboxTable.setItems(getRequestsInbox(loggedInVolunteer.getUsername()));
        if (this.loggedInVolunteer.isNewOffer() == true) {
            requestsButton.setStyle("-fx-background-color: #44919c;");
            requestsButton.setPrefWidth(115);
            requestsButton.setText("Requests (new)");
            UserService.updateUserInDatabase(loggedInVolunteer);
        }
        if (this.loggedInVolunteer.isNewHistory() == true) {
            historyButton.setStyle("-fx-background-color: #44919c;");
            historyButton.setPrefWidth(115);
            historyButton.setText("History (new)");
            UserService.updateUserInDatabase(loggedInVolunteer);
        }
        historyTable.setItems(getHistory(loggedInVolunteer.getUsername()));
        handleHomePage();
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

        TableColumn<Affected, String> affectedUsernameColumn = new TableColumn<>("Username");
        affectedUsernameColumn.setMinWidth(200);
        affectedUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Affected, String> affectedFullNameColumn = new TableColumn<>("Full Name");
        affectedFullNameColumn.setMinWidth(200);
        affectedFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));;
        TableColumn<Affected, String> affectedCountryColumn = new TableColumn<>("Country");
        affectedCountryColumn.setMinWidth(200);
        affectedCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        affectedTable.getColumns().addAll(affectedUsernameColumn, affectedFullNameColumn, affectedCountryColumn);

        TableColumn<Communication, String> communicationType = new TableColumn<>("Type");
        communicationType.setMinWidth(100);
        communicationType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Communication, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Communication, String> p ) {
                return new ReadOnlyStringWrapper(p.getValue().isType() == 'r' ? "Request" : "Offer");
            }
        });
        TableColumn<Communication, Integer> communicationSource = new TableColumn<>("Source");
        communicationSource.setMinWidth(100);
        communicationSource.setCellValueFactory(new PropertyValueFactory<>("sourceUsername"));
        TableColumn<Communication, Integer> communicationDestination = new TableColumn<>("Destination");
        communicationDestination.setMinWidth(100);
        communicationDestination.setCellValueFactory(new PropertyValueFactory<>("destinationUsername"));
        TableColumn<Communication, String> communicationStatus = new TableColumn<>("Status");
        communicationStatus.setMinWidth(100);
        communicationStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Communication, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Communication, String> p ) {
                return new ReadOnlyStringWrapper(p.getValue().getStatus() == 'a' ? "Accepted" : "Rejected");
            }
        });
        TableColumn<Communication, Character> communicationSourceMessage = new TableColumn<>("Your message");
        communicationSourceMessage.setMinWidth(200);
        communicationSourceMessage.setCellValueFactory(new PropertyValueFactory<>("sourceMessage"));
        TableColumn<Communication, Character> communicationSourceContactMethods = new TableColumn<>("Your contact methods");
        communicationSourceContactMethods.setMinWidth(200);
        communicationSourceContactMethods.setCellValueFactory(new PropertyValueFactory<>("sourceContactMethods"));
        TableColumn<Communication, Character> communicationDestinationMessage = new TableColumn<>("Volunteer's message");
        communicationDestinationMessage.setMinWidth(200);
        communicationDestinationMessage.setCellValueFactory(new PropertyValueFactory<>("destinationMessage"));
        TableColumn<Communication, Character> communicationDestinationContactMethods = new TableColumn<>("Volunteer's contact methods");
        communicationDestinationContactMethods.setMinWidth(200);
        communicationDestinationContactMethods.setCellValueFactory(new PropertyValueFactory<>("destinationContactMethods"));
        historyTable.getColumns().addAll(communicationType, communicationSource, communicationDestination, communicationStatus, communicationSourceMessage, communicationSourceContactMethods, communicationDestinationMessage, communicationDestinationContactMethods);


    }
    public void handleHomePage() {
        loggedInVolunteer.calculateValues();
        signedInAsLabel.setText("Welcome, " + loggedInVolunteer.getFullName() + "!");
        signedInAsLabel11.setText("You currently have: " + loggedInVolunteer.getOffersNo() + " Offers in your Offer list.");
        signedInAsLabel1.setText("There are currently " + affectedTable.getItems().size() + " Affected available in your country.");
        signedInAsLabel111.setText(loggedInVolunteer.getActiveRequestsNo() + " Requests in your Request inbox.");
        homeTab.setManaged(true);
        homeTab.setVisible(true);
        offersTab.setManaged(false);
        offersTab.setVisible(false);
        requestsInboxTab.setManaged(false);
        requestsInboxTab.setVisible(false);
        affectedTab.setVisible(false);
        affectedTab.setManaged(false);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
    }
    public void handleOffersPage() {
        homeTab.setManaged(false);
        homeTab.setVisible(false);
        offersTab.setManaged(true);
        offersTab.setVisible(true);
        requestsInboxTab.setManaged(false);
        requestsInboxTab.setVisible(false);
        affectedTab.setVisible(false);
        affectedTab.setManaged(false);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
    }


    public void handleRequestsInboxPage() {
        homeTab.setManaged(false);
        homeTab.setVisible(false);
        offersTab.setManaged(false);
        offersTab.setVisible(false);
        requestsInboxTab.setManaged(true);
        requestsInboxTab.setVisible(true);
        affectedTab.setVisible(false);
        affectedTab.setManaged(false);
        if (loggedInVolunteer.isNewOffer() == true) {
            this.loggedInVolunteer.setNewOffer(false);
            requestsButton.setStyle("-fx-background-color: #d6eaed;");
            requestsButton.setPrefWidth(102);
            requestsButton.setText("Requests");
            UserService.updateUserInDatabase(this.loggedInVolunteer);

        }
        historyTab.setVisible(false);
        historyTab.setManaged(false);
    }

    public void handleAffectedPage() {
        homeTab.setManaged(false);
        homeTab.setVisible(false);
        offersTab.setManaged(false);
        offersTab.setVisible(false);
        requestsInboxTab.setManaged(false);
        requestsInboxTab.setVisible(false);
        affectedTab.setVisible(true);
        affectedTab.setManaged(true);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
    }

    public void handleHistoryPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        offersTab.setVisible(false);
        offersTab.setManaged(false);
        affectedTab.setVisible(false);
        affectedTab.setManaged(false);
        offersTab.setVisible(false);
        offersTab.setManaged(false);
        historyTab.setVisible(true);
        historyTab.setManaged(true);
        if (loggedInVolunteer.isNewHistory() == true) {
            this.loggedInVolunteer.setNewHistory(false);
            historyButton.setStyle("-fx-background-color: #d6eaed;");
            historyButton.setPrefWidth(102);
            historyButton.setText("History");
            UserService.updateUserInDatabase(this.loggedInVolunteer);

        }
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

    public static ObservableList<Affected> getAffected(String country) {
        return FXCollections.observableList(UserService.affectedToList(country));
    }

    public static ObservableList<AffectedItem> getRequestsInbox(String username) {
        return FXCollections.observableList(AffectedService.databaseToListInbox(CommunicationService.getSourceIDs(username)));
    }

    public static ObservableList<Communication> getHistory(String username) {
        return FXCollections.observableList(CommunicationService.getHistory(username));
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
        if (requestsInboxTable.getSelectionModel().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Select a request in order to view image", "Failed to open image", 1);
            return;
        }
        else if (requestInboxImage == null) {
            JOptionPane.showMessageDialog(null, "This request has no image", "Failed to open image", 1);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("requestImageDialog.fxml"));
        Parent imageDialog = loader.load();
        RequestImageDialogController controller = loader.getController();
        controller.setRequestImage(requestInboxImage);
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
        }
    }

    public void handleContactAffected() throws IOException{
        if (affectedTable.getSelectionModel().getSelectedItem() != null) {
            Affected toBeContacted = (Affected) affectedTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("contactAffectedForm.fxml"));
            Parent imageDialog = loader.load();
            AffectedContactController controller = loader.getController();
            controller.setLoggedInVolunteer(this.loggedInVolunteer, toBeContacted);
            Scene scene = new Scene(imageDialog);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setTitle("ShelterMe - Contact " + toBeContacted.getFullName());
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select an Affected person to contact", "Failed to contact Affected", 1);
        }
    }

    public void handleAffectedInfo() throws IOException {
        if (affectedTable.getSelectionModel().getSelectedItem() != null) {
            Affected toBeContacted = (Affected) affectedTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("viewAffectedRequests.fxml"));
            Parent imageDialog = loader.load();
            ViewAffectedRequestsController controller = loader.getController();
            controller.setAffected(toBeContacted);
            Scene scene = new Scene(imageDialog);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setTitle("ShelterMe - View " + toBeContacted.getFullName() + "'s info");
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select an Affected to view info", "Failed to view info of Affected", 1);
        }
    }

    public void handleShowItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("historyItemView.fxml"));
        Parent history = loader.load();
        HistoryItemViewController newController = loader.getController();
        newController.setLoggedInUser(loggedInVolunteer);
        newController.setHistoryImage(historyImage);
        newController.setHistoryTable(historyTable);
        Scene scene = new Scene(history);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(offersTab.getScene().getWindow());
        newStage.setTitle("ShelterMe - History View");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);
    }

    public void handleHistoryTableClick(MouseEvent event) throws IOException {
        if (historyTable.getSelectionModel().getSelectedItem() != null) {
            Communication com = ((Communication)historyTable.getSelectionModel().getSelectedItem());
            if (com.isType() == 'r') {
                AffectedItem item = AffectedService.getItemWithId(com.getId());
                historyImage = AffectedService.base64ToImage(item.getImageBase64());
            } else {
                VolunteerItem item = VolunteerService.getItemWithId(com.getId());
                historyImage = VolunteerService.base64ToImage(item.getImageBase64());
            }
        } else historyImage = null;
    }

}