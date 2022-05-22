package org.ShelterMe.project.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.CommunicationExistsException;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.VolunteerService;
import org.ShelterMe.project.services.CommunicationService;
import org.jetbrains.annotations.NotNull;


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
    private Alert alert;

    public void setLoggedInVolunteer(@NotNull Volunteer loggedInVolunteer, @NotNull Affected toBeContacted) {
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
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Offers sent");
                alert.setHeaderText("Offer sent successfully to Affected (" + toBeContacted.getFullName() + ")");
                alert.showAndWait();
                stage.close();
            } else {
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.setAlwaysOnTop(false);
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failed to contact Affected");
                alert.setHeaderText("Select an offer to send!");
                alert.showAndWait();
                stage.setAlwaysOnTop(true);
            }
        } catch(CommunicationExistsException e) {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to contact Affected");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            stage.setAlwaysOnTop(true);
        }
    }

    public Alert getAlert()
    {
        return alert;
    }

    public TableView getContactOffersView() {
        return contactOffersView;
    }
}