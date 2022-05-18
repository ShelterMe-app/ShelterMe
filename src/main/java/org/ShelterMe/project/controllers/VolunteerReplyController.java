package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.CommunicationService;
import org.ShelterMe.project.services.AffectedService;

import javax.swing.*;
import java.awt.*;

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

    public void setVolunteerReplyName(Text volunteerReplyName) {
        this.volunteerReplyName = volunteerReplyName;
    }

    public void setMessageToVolunteer(Text messageToVolunteer) {
        this.messageToVolunteer = messageToVolunteer;
    }

    public void setContactToVolunteer(Text contactToVolunteer) {
        this.contactToVolunteer = contactToVolunteer;
    }

    public void handleApproveRequest(javafx.event.ActionEvent event) {
        CommunicationService.closeRequest(loggedInVolunteer.getUsername(), AffectedService.getRequestDestinationUsername(requestId), requestId, 'a');
        if (requestsInboxTable != null)
            requestsInboxTable.setItems(VolunteerPageController.getRequestsInbox(loggedInVolunteer.getUsername()));
        Stage stage = (Stage) approveRequestButton.getScene().getWindow();
        stage.close();
        JOptionPane.showMessageDialog(null, AffectedService.getRequestName(requestId) + " request approved", "Successfully approved request", 1);
    }

    public void handleRejectRequest(javafx.event.ActionEvent event){
        CommunicationService.closeRequest(loggedInVolunteer.getUsername(), AffectedService.getRequestDestinationUsername(requestId), requestId, 'r');
        if (requestsInboxTable != null)
            requestsInboxTable.setItems(VolunteerPageController.getRequestsInbox(loggedInVolunteer.getUsername()));
        Stage stage = (Stage) rejectRequestButton.getScene().getWindow();
        stage.close();
        JOptionPane.showMessageDialog(null, AffectedService.getRequestName(requestId) + " request rejected", "Successfully rejected request", 1);
    }
}
