package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.CommunicationService;

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

    }

    public void handleRejectRequest(javafx.event.ActionEvent event){

    }
}
