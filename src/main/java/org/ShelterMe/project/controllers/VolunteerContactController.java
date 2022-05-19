package org.ShelterMe.project.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Communication;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.CommunicationService;

import javax.swing.*;

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
        if (contactRequestsView.getSelectionModel().getSelectedItem() != null) {
            CommunicationService.addCommunication('r', loggedInAffected.getUsername(), toBeContacted.getUsername(), ((AffectedItem)contactRequestsView.getSelectionModel().getSelectedItem()).getId(), 'p', message.getText(), "", contactMethods.getText(), "");
            JOptionPane.showMessageDialog(null, "Request sent succesfully to Volunteer (" + toBeContacted.getFullName() + ")", "Request sent", 1);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } else {
            JOptionPane.showMessageDialog(null, "Select a request to send", "Failed to contact Volunteer", 1);
        }
    }
}
