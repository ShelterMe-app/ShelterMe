package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.CommunicationService;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.UserService;

public class VolunteerReplyController {

    private Volunteer loggedInVolunteer;
    private TableView requestsInboxTable;
    private int requestId;
    @FXML
    private Text volunteerReplyName;
    @FXML
    private Text messageToVolunteer;
    @FXML
    private Text contactToVolunteer;
    @FXML
    private TextArea volunteerReplyMessage;
    @FXML
    private TextArea volunteerContactInfo;
    @FXML
    private Button approveRequestButton;
    @FXML
    private Button rejectRequestButton;

    public void setRequestId(int requestId) {
        this.requestId = requestId;
        volunteerReplyName.setText(CommunicationService.getSourceName(requestId) + "'s request");
        messageToVolunteer.setText(CommunicationService.getSourceDescription(requestId));
        contactToVolunteer.setText(CommunicationService.getSourceInfo(requestId));
    }

    public void setLoggedInVolunteer(Volunteer loggedInVolunteer) {
        this.loggedInVolunteer = loggedInVolunteer;
    }

    public void setRequestsInboxTable(TableView requestsInboxTable) {
        this.requestsInboxTable = requestsInboxTable;
    }

    public void handleApproveRequest(javafx.event.ActionEvent event) {
        CommunicationService.closeRequest(AffectedService.getRequestDestinationUsername(requestId), loggedInVolunteer.getUsername(), requestId, 'a', volunteerReplyMessage.getText(), volunteerContactInfo.getText());
        Affected destination = (Affected) UserService.getUser(((AffectedItem)requestsInboxTable.getSelectionModel().getSelectedItem()).getUsername());
        if (requestsInboxTable != null)
            requestsInboxTable.setItems(VolunteerPageController.getRequestsInbox(loggedInVolunteer.getUsername()));
        Stage stage = (Stage) approveRequestButton.getScene().getWindow();
        stage.close();
        destination.setNewHistory(true);
        UserService.updateUserInDatabase(destination);
        loggedInVolunteer.setNewHistory(true);
        UserService.updateUserInDatabase(loggedInVolunteer);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully approved request");
        alert.setHeaderText(AffectedService.getRequestName(requestId) + " request approved");
        alert.showAndWait();
    }

    public void handleRejectRequest(javafx.event.ActionEvent event){
        CommunicationService.closeRequest(AffectedService.getRequestDestinationUsername(requestId), loggedInVolunteer.getUsername(), requestId, 'r', volunteerReplyMessage.getText(), volunteerContactInfo.getText());
        Affected destination = (Affected) UserService.getUser(((AffectedItem)requestsInboxTable.getSelectionModel().getSelectedItem()).getUsername());
        if (requestsInboxTable != null)
            requestsInboxTable.setItems(VolunteerPageController.getRequestsInbox(loggedInVolunteer.getUsername()));
        Stage stage = (Stage) rejectRequestButton.getScene().getWindow();
        stage.close();
        destination.setNewHistory(true);
        UserService.updateUserInDatabase(destination);
        loggedInVolunteer.setNewHistory(true);
        UserService.updateUserInDatabase(loggedInVolunteer);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully rejected request");
        alert.setHeaderText(AffectedService.getRequestName(requestId) + " request rejected");
        alert.showAndWait();
    }
}
