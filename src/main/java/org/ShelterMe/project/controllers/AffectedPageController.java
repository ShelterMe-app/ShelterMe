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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ShelterMe.project.services.UserService;

import java.io.IOException;

import javafx.util.Callback;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.model.AffectedItem;

import javax.swing.JOptionPane;

import org.ShelterMe.project.model.Affected;

import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.services.VolunteerService;
import org.ShelterMe.project.services.CommunicationService;

import org.ShelterMe.project.model.Communication;
import org.jetbrains.annotations.NotNull;

public class AffectedPageController{
    private Affected loggedInAffected;
    @FXML
    private Label signedInAsLabel;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private JFXButton historyButton;
    @FXML
    private JFXButton offersButton;
    @FXML
    private VBox homeTab;
    @FXML
    private VBox requestsTab;
    @FXML
    private VBox volunteersTab;
    @FXML
    private VBox offersTab;
    @FXML
    private VBox historyTab;
    @FXML
    private TableView requestsTable;
    @FXML
    private TableView volunteersTable;
    @FXML
    private TableView offersInboxTable;
    @FXML
    private TableView historyTable;
    @FXML
    private Label signedInAsLabel1;
    @FXML
    private Label signedInAsLabel11;
    @FXML
    private Label signedInAsLabel111;

    private Image requestImage;

    private Image offerInboxImage;

    private Image historyImage;

    public void setSignedInAs(@NotNull Affected loggedInAffected) {
        this.loggedInAffected = loggedInAffected;
        signedInAsLabel.setText("Welcome, " + loggedInAffected.getFullName() + "!");
        requestsTable.setItems(getRequests(loggedInAffected.getUsername()));
        volunteersTable.setItems(getVolunteers(loggedInAffected.getCountry()));
        offersInboxTable.setItems(getOffersInbox(loggedInAffected.getUsername()));
        historyTable.setItems(getHistory(loggedInAffected.getUsername()));
        if (this.loggedInAffected.isNewHistory() == true) {
            historyButton.setStyle("-fx-background-color: #44919c;");
            historyButton.setPrefWidth(115);
            historyButton.setText("History (new)");
            UserService.updateUserInDatabase(loggedInAffected);
        }
        handleHomePage();
        if (this.loggedInAffected.isNewRequest() == true) {
            offersButton.setStyle("-fx-background-color: #44919c;");
            offersButton.setPrefWidth(115);
            offersButton.setText("Offers (new)");
            UserService.updateUserInDatabase(loggedInAffected);
        }
    }

    public void handleSignOut(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        GotoLoginController.goToLogin(stage, event);
    }

    @FXML
    public void initialize() {
        TableColumn<AffectedItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<AffectedItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(200);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<AffectedItem, String> suppliesColumn = new TableColumn<>("Supplies");
        suppliesColumn.setMinWidth(200);
        suppliesColumn.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<AffectedItem, Float> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(200);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<AffectedItem, String> generalInformationColumn = new TableColumn<>("General Information");
        generalInformationColumn.setMinWidth(200);
        generalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("generalInformation"));
        TableColumn<AffectedItem, String> healthConditionColumn = new TableColumn<>("Health Condition");
        healthConditionColumn.setMinWidth(200);
        healthConditionColumn.setCellValueFactory(new PropertyValueFactory<>("healthCondition"));
        requestsTable.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn, generalInformationColumn, healthConditionColumn);

        TableColumn<Volunteer, String> volunteerUsernameColumn = new TableColumn<>("Username");
        volunteerUsernameColumn.setMinWidth(200);
        volunteerUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<Volunteer, String> volunteerFullNameColumn = new TableColumn<>("Full Name");
        volunteerFullNameColumn.setMinWidth(200);
        volunteerFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));;
        TableColumn<Volunteer, String> volunteerCountryColumn = new TableColumn<>("Country");
        volunteerCountryColumn.setMinWidth(200);
        volunteerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        volunteersTable.getColumns().addAll(volunteerUsernameColumn, volunteerFullNameColumn, volunteerCountryColumn);
        TableColumn<VolunteerItem, String> usernameColumnVolunteer = new TableColumn<>("Username");
        usernameColumnVolunteer.setMinWidth(200);
        usernameColumnVolunteer.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<VolunteerItem, String> nameColumnVolunteer = new TableColumn<>("Name");
        nameColumnVolunteer.setMinWidth(200);
        nameColumnVolunteer.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<VolunteerItem, String> categoryColumnVolunteer = new TableColumn<>("Category");
        categoryColumnVolunteer.setMinWidth(200);
        categoryColumnVolunteer.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<VolunteerItem, String> suppliesColumnVolunteer = new TableColumn<>("Supplies");
        suppliesColumnVolunteer.setMinWidth(200);
        suppliesColumnVolunteer.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<VolunteerItem, Float> quantityColumnVolunteer = new TableColumn<>("Quantity");
        quantityColumnVolunteer.setMinWidth(200);
        quantityColumnVolunteer.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        offersInboxTable.getColumns().addAll(usernameColumnVolunteer, nameColumnVolunteer, categoryColumnVolunteer, suppliesColumnVolunteer, quantityColumnVolunteer);
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
        TableColumn<Communication, Character> communicationSourceMessage = new TableColumn<>("Source message");
        communicationSourceMessage.setMinWidth(200);
        communicationSourceMessage.setCellValueFactory(new PropertyValueFactory<>("sourceMessage"));
        TableColumn<Communication, Character> communicationSourceContactMethods = new TableColumn<>("Source contact methods");
        communicationSourceContactMethods.setMinWidth(200);
        communicationSourceContactMethods.setCellValueFactory(new PropertyValueFactory<>("sourceContactMethods"));
        TableColumn<Communication, Character> communicationDestinationMessage = new TableColumn<>("Destination message");
        communicationDestinationMessage.setMinWidth(200);
        communicationDestinationMessage.setCellValueFactory(new PropertyValueFactory<>("destinationMessage"));
        TableColumn<Communication, Character> communicationDestinationContactMethods = new TableColumn<>("Destination contact methods");
        communicationDestinationContactMethods.setMinWidth(200);
        communicationDestinationContactMethods.setCellValueFactory(new PropertyValueFactory<>("destinationContactMethods"));
        historyTable.getColumns().addAll(communicationType, communicationSource, communicationDestination, communicationStatus, communicationSourceMessage, communicationSourceContactMethods, communicationDestinationMessage, communicationDestinationContactMethods);

    }



    public void handleHomePage() {
        loggedInAffected.calculateValues();
        signedInAsLabel1.setText("There are currently " + volunteersTable.getItems().size() + " Volunteers available in your country.");
        signedInAsLabel11.setText("You currently have: " + loggedInAffected.getRequestsNo() + " Requests in your Requests list.");
        signedInAsLabel111.setText(offersInboxTable.getItems().size() + " Offers in your Offers list.");
        homeTab.setVisible(true);
        homeTab.setManaged(true);
        requestsTab.setVisible(false);
        requestsTab.setManaged(false);
        volunteersTab.setVisible(false);
        volunteersTab.setManaged(false);
        offersTab.setVisible(false);
        offersTab.setManaged(false);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
    }
    public void handleRequestsPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        volunteersTab.setVisible(false);
        volunteersTab.setManaged(false);
        offersTab.setVisible(false);
        offersTab.setManaged(false);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
        requestsTab.setVisible(true);
        requestsTab.setManaged(true);
    }

    public void handleVolunteersPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        requestsTab.setVisible(false);
        requestsTab.setManaged(false);
        offersTab.setVisible(false);
        offersTab.setManaged(false);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
        volunteersTab.setVisible(true);
        volunteersTab.setManaged(true);
    }

    public void handleOffersPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        requestsTab.setVisible(false);
        requestsTab.setManaged(false);
        volunteersTab.setVisible(false);
        volunteersTab.setManaged(false);
        historyTab.setVisible(false);
        historyTab.setManaged(false);
        offersTab.setVisible(true);
        offersTab.setManaged(true);
        if (loggedInAffected.isNewRequest() == true) {
            this.loggedInAffected.setNewRequest(false);
            offersButton.setStyle("-fx-background-color: #d6eaed;");
            offersButton.setPrefWidth(102);
            offersButton.setText("Requests");
            UserService.updateUserInDatabase(this.loggedInAffected);
        }
    }

    public void handleHistoryPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        requestsTab.setVisible(false);
        requestsTab.setManaged(false);
        volunteersTab.setVisible(false);
        volunteersTab.setManaged(false);
        offersTab.setVisible(false);
        offersTab.setManaged(false);
        historyTab.setVisible(true);
        historyTab.setManaged(true);
        if (historyTable != null)
            historyTable.setItems(getHistory(loggedInAffected.getUsername()));
        if (loggedInAffected.isNewHistory() == true) {
            this.loggedInAffected.setNewHistory(false);
            historyButton.setStyle("-fx-background-color: #d6eaed;");
            historyButton.setPrefWidth(102);
            historyButton.setText("History");
            UserService.updateUserInDatabase(this.loggedInAffected);

        }
    }

    public void handleAddRequest(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addRequestForm.fxml"));
        Parent addRequest = loader.load();
        RequestMenuController newController = loader.getController();
        newController.setLoggedInAffected(loggedInAffected);
        newController.setRequestsTable(requestsTable);
        newController.setRemoveCurrentImageStatus(false);
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

    public static ObservableList<AffectedItem> getRequests(String username) {

        return FXCollections.observableList(AffectedService.databaseToList(username));
    }

    public static ObservableList<Volunteer> getVolunteers(String country) {
        return FXCollections.observableList(UserService.volunteersToList(country));
    }

    public static ObservableList<VolunteerItem> getOffersInbox(String username) {
        return FXCollections.observableList(VolunteerService.databaseToListInbox(CommunicationService.getSourceIDs(username)));
    }

    public static ObservableList<Communication> getHistory(String username) {
        return FXCollections.observableList(CommunicationService.getHistory(username));
    }


    public void handleTableClick(MouseEvent event) throws IOException  {
        if (requestsTable.getSelectionModel().getSelectedItem() != null)
            requestImage = AffectedService.base64ToImage(((AffectedItem)requestsTable.getSelectionModel().getSelectedItem()).getImageBase64());
    }

    public void handleRequestImage(javafx.event.ActionEvent event) throws IOException {
        if (requestImage == null) {
            JOptionPane.showMessageDialog(null, "This request has no image", "Failed to open image", 1);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("requestImageDialog.fxml"));
        Parent imageDialog = loader.load();
        RequestImageDialogController controller = loader.getController();
        controller.setRequestImage(requestImage);
        Scene scene = new Scene(imageDialog);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setTitle("ShelterMe - Request image");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);

    }

    public void handleEditRequest(javafx.event.ActionEvent event) throws IOException {
        if (requestsTable.getSelectionModel().getSelectedItem() != null) {
            AffectedItem request = (AffectedItem)requestsTable.getSelectionModel().getSelectedItem();
            int requestId = request.getId();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addRequestForm.fxml"));
            Parent editRequest = loader.load();
            RequestMenuController newController = loader.getController();
            newController.setLoggedInAffected(loggedInAffected);
            newController.setRequestsTable(requestsTable);
            if (request.getImageBase64() != null && request.getImageBase64().length() > 0)
                newController.setRemoveCurrentImageStatus(true);
            else
                newController.setRemoveCurrentImageStatus(false);
            newController.setRequestId(requestId);
            newController.setRequestMenuWelcomeText("Edit Request");
            newController.setAddRequestButtonText("Edit request");
            newController.setRequestNamePlaceholder(request.getName());
            newController.setRequestCategoryPlaceholder(request.getCategory());
            newController.setRequestSuppliesPlaceholder(request.getSupplies());
            newController.setRequestQuantityPlaceholder(Float.toString(request.getQuantity()));
            newController.setGeneralInformationPlaceholder(request.getGeneralInformation());
            newController.setHealthConditionPlaceholder(request.getHealthCondition());
            newController.setRequestImagePlaceholder("Update image");
            Scene scene = new Scene(editRequest);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(requestsTab.getScene().getWindow());
            newStage.setTitle("ShelterMe - Edit a Request");
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
            requestImage = null;
        } else {
            JOptionPane.showMessageDialog(null, "Select a request in order to edit", "Failed to edit request", 1);
        }
    }

    public void handleRemoveRequest(javafx.event.ActionEvent event) throws IOException {
        if (requestsTable.getSelectionModel().getSelectedItem() != null) {
            AffectedItem request = (AffectedItem)requestsTable.getSelectionModel().getSelectedItem();
            int requestId = request.getId();
            AffectedService.removeItem(requestId);
            JOptionPane.showMessageDialog(null, "Selected request has been removed", "Succesfully removed request", 1);
            if (requestsTable != null)
                requestsTable.setItems(AffectedPageController.getRequests(loggedInAffected.getUsername()));
        } else {
            JOptionPane.showMessageDialog(null, "Select a request in order to remove", "Failed to remove request", 1);
        }
    }

    public void handleContactVolunteer() throws IOException {
        if (volunteersTable.getSelectionModel().getSelectedItem() != null) {
            Volunteer toBeContacted = (Volunteer)volunteersTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("contactVolunteerForm.fxml"));
            Parent imageDialog = loader.load();
            VolunteerContactController controller = loader.getController();
            controller.setLoggedInAffected(this.loggedInAffected, toBeContacted);
            Scene scene = new Scene(imageDialog);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setTitle("ShelterMe - Contact " + toBeContacted.getFullName());
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select a Volunteer to contact", "Failed to contact Volunteer", 1);
        }
    }

    public void handleVolunteerInfo() throws IOException {
        if (volunteersTable.getSelectionModel().getSelectedItem() != null) {
            Volunteer toBeContacted = (Volunteer)volunteersTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("viewVolunteerOffers.fxml"));
            Parent imageDialog = loader.load();
            ViewVolunteerOffersController controller = loader.getController();
            controller.setVolunteer(toBeContacted);
            Scene scene = new Scene(imageDialog);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setTitle("ShelterMe - View " + toBeContacted.getFullName() + "'s info");
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select a Volunteer to view info", "Failed to view info of Volunteer", 1);
        }
    }

    public void handleOfferInboxTableClick(MouseEvent event) throws IOException  {
        if (offersInboxTable.getSelectionModel().getSelectedItem() != null)
            offerInboxImage = VolunteerService.base64ToImage(((VolunteerItem)offersInboxTable.getSelectionModel().getSelectedItem()).getImageBase64());
    }
    public void handleOfferInboxImage(javafx.event.ActionEvent event) throws IOException {
        if (offersInboxTable.getSelectionModel().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Select an offer in order to view image", "Failed to open image", 1);
            return;
        }
        else if (offerInboxImage == null) {
            JOptionPane.showMessageDialog(null, "This offer has no image", "Failed to open image", 1);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("offerImageDialog.fxml"));
        Parent imageDialog = loader.load();
        OfferImageDialogController controller = loader.getController();
        controller.setOfferImage(offerInboxImage);
        Scene scene = new Scene(imageDialog);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setTitle("ShelterMe - Offer image");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);
    }

    public void handleShowMessage() throws IOException {
        if (offersInboxTable.getSelectionModel().getSelectedItem() != null)
        {
            VolunteerItem connection = (VolunteerItem) offersInboxTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("affectedReplyForm.fxml"));
            Parent viewRequestInbox = loader.load();
            AffectedReplyController newController = loader.getController();
            newController.setLoggedInAffected(loggedInAffected);
            newController.setOffersInboxTable(offersInboxTable);
            newController.setOfferId(connection.getId());
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
            JOptionPane.showMessageDialog(null, "Select an offer in order to see it", "Failed to open offer", 1);
        }
    }

    public void handleShowItem() throws IOException {
        if (historyTable.getSelectionModel().getSelectedItem() != null) {
            Object o = historyTable.getSelectionModel().getSelectedItem();
            Communication com = (Communication)o;
            Object item = (com.isType() == 'r' ? AffectedService.getItemWithId(com.getId()) : VolunteerService.getItemWithId(com.getId()));
            if (item != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("historyItemView.fxml"));
                Parent history = loader.load();
                HistoryItemViewController newController = loader.getController();
                newController.setLoggedInUser(loggedInAffected);
                newController.setHistoryImage(historyImage);
                newController.setHistoryTable(historyTable);
                Scene scene = new Scene(history);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(requestsTab.getScene().getWindow());
                newStage.setTitle("ShelterMe - History View");
                newStage.getIcons().add(new Image("file:docs/Logo.png"));
                newStage.show();
                newStage.setResizable(false);
            } else {
                JOptionPane.showMessageDialog(null, "Item has been deleted by it's owner", "Failed to show item", 1);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Select an item to show first", "Failed to show item", 1);
        }

    }

    public void handleHistoryTableClick(MouseEvent event) throws IOException {
        if (historyTable.getSelectionModel().getSelectedItem() != null) {
            Communication com = ((Communication)historyTable.getSelectionModel().getSelectedItem());
            if (com.isType() == 'r') {
                AffectedItem item = AffectedService.getItemWithId(com.getId());
                if (item != null)
                    historyImage = AffectedService.base64ToImage(item.getImageBase64());
            } else {
                VolunteerItem item = VolunteerService.getItemWithId(com.getId());
                if (item != null)
                    historyImage = VolunteerService.base64ToImage(item.getImageBase64());
            }
        } else historyImage = null;
    }
}
