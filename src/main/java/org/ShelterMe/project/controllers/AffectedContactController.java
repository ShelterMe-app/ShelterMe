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
import org.ShelterMe.project.exceptions.CommunicationExistsException;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.model.Communication;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.VolunteerService;
import org.ShelterMe.project.services.CommunicationService;

import javax.swing.*;

public class AffectedContactController {
    Volunteer loggedInVolunteer;
    Affected toBeContacted;

    @FXML
    private Text contactAffectedWelcome;
    @FXML
    private TableView contactOffersView;
    @FXML
    private TextField contactMethods;
    @FXML
    private TextArea message;

    public void setLoggedInVolunteer(Volunteer loggedInVolunteer, Affected toBeContacted) {
        this.loggedInVolunteer = loggedInVolunteer;
        this.toBeContacted = toBeContacted;
        contactOffersView.setItems(FXCollections.observableList(VolunteerService.databaseToList(loggedInVolunteer.getUsername())));
        contactAffectedWelcome.setText("Contact " + toBeContacted.getFullName());
    }
    @FXML
    public void initialize() {
        TableColumn<VolunteerItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<VolunteerItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(200);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<VolunteerItem, String> suppliesColumn = new TableColumn<>("Supplies");
        suppliesColumn.setMinWidth(200);
        suppliesColumn.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<VolunteerItem, Float> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(200);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        contactOffersView.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn);
    }

    public void handleSendOfferAction(javafx.event.ActionEvent event) {
        try {
            if (contactOffersView.getSelectionModel().getSelectedItem() != null) {
                CommunicationService.existsCommunication(((VolunteerItem)contactOffersView.getSelectionModel().getSelectedItem()).getId(), loggedInVolunteer.getUsername(), toBeContacted.getUsername(), "offer", "Affected");
                CommunicationService.addCommunication('o', loggedInVolunteer.getUsername(), toBeContacted.getUsername(), ((VolunteerItem) contactOffersView.getSelectionModel().getSelectedItem()).getId(), 'p', message.getText(), "", contactMethods.getText(), "");
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, "Offer sent successfully to Affected (" + toBeContacted.getFullName() + ")", "Offers sent", 1);
                stage.close();

            } else {
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, "Select an offer to send", "Failed to contact Affected", 1);
                stage.setAlwaysOnTop(true);
            }
        } catch(CommunicationExistsException e) {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed to contact Affected", 1);
            stage.setAlwaysOnTop(true);
        }
    }
}