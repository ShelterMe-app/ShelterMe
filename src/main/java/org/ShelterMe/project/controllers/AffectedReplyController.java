package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.CommunicationService;

import java.awt.*;

public class AffectedReplyController {

    private Affected loggedInAffected;
    private TableView offersInboxTable;
    private int requestId;
    @FXML
    private Text affectedReplyName;
    @FXML
    private Text messageToAffected;
    @FXML
    private Text contactToAffected;
    @FXML
    private Button approveOfferButton;
    @FXML
    private Button rejectOfferButton;

    public void setRequestId(int requestId) {
        this.requestId = requestId;
        affectedReplyName.setText(CommunicationService.getSourceName(requestId) + "'s offer");
        messageToAffected.setText(CommunicationService.getSourceDescription(requestId));
        contactToAffected.setText(CommunicationService.getSourceInfo(requestId));
    }

    public void setLoggedInAffected(Affected loggedInAffected) {
        this.loggedInAffected = loggedInAffected;
    }

    public void setOffersInboxTable(TableView offersInboxTable) {
        this.offersInboxTable = offersInboxTable;
    }

    public void setAffectedReplyName(Text affectedReplyName) {
        this.affectedReplyName = affectedReplyName;
    }

    public void setMessageToAffected(Text messageToAffected) {
        this.messageToAffected = messageToAffected;
    }

    public void setContactToAffected(Text contactToAffected) {
        this.contactToAffected = contactToAffected;
    }

    public void handleApproveOffer(javafx.event.ActionEvent event) {

    }

    public void handleRejectOffer(javafx.event.ActionEvent event){

    }
}