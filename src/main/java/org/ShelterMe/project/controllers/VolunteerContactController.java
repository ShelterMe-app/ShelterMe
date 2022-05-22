package org.ShelterMe.project.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.CommunicationExistsException;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.CommunicationService;

public class VolunteerContactController {
    Affected loggedInAffected;
    Volunteer toBeContacted;

    @FXML
    private Text contactVolunteerWelcome;
    @FXML
    private TableView contactRequestsView;
    @FXML
    private TextField contactMethods;
    @FXML
    private TextArea message;
    private Alert alert;

    public void setLoggedInAffected(Affected loggedInAffected, Volunteer toBeContacted) {
        this.loggedInAffected = loggedInAffected;
        this.toBeContacted = toBeContacted;
        contactRequestsView.setItems(FXCollections.observableList(AffectedService.databaseToList(loggedInAffected.getUsername())));
        contactVolunteerWelcome.setText("Contact " + toBeContacted.getFullName());
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
        contactRequestsView.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn);
    }

    public void handleSendRequestAction(javafx.event.ActionEvent event) {
        try {
            if (contactRequestsView.getSelectionModel().getSelectedItem() != null) {
                CommunicationService.existsCommunication(((AffectedItem)contactRequestsView.getSelectionModel().getSelectedItem()).getId(), loggedInAffected.getUsername(), toBeContacted.getUsername(), "request", "Volunteer");
                CommunicationService.addCommunication('r', loggedInAffected.getUsername(), toBeContacted.getUsername(), ((AffectedItem)contactRequestsView.getSelectionModel().getSelectedItem()).getId(), 'p', message.getText(), "", contactMethods.getText(), "");
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.setAlwaysOnTop(false);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Requests sent");
                alert.setHeaderText("Request sent successfully to Volunteer (" + toBeContacted.getFullName() + ")");
                alert.showAndWait();
                stage.close();
            } else {
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.setAlwaysOnTop(false);
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failed to contact Volunteer");
                alert.setHeaderText("Select a request to send!");
                alert.showAndWait();
                stage.setAlwaysOnTop(true);
            }
        }catch(CommunicationExistsException e) {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to contact Volunteer");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            stage.setAlwaysOnTop(true);
        }
    }

    public Alert getAlert()
    {
        return alert;
    }

    public TableView getContactRequestsView() {
        return contactRequestsView;
    }
}
