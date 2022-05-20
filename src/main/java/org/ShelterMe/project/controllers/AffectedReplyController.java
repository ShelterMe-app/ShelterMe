package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.CommunicationService;
import javafx.stage.Stage;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.services.VolunteerService;

import javax.swing.*;
import java.awt.*;
import org.ShelterMe.project.services.UserService;

public class AffectedReplyController {

    private Affected loggedInAffected;
    private TableView offersInboxTable;
    private int offerId;
    @FXML
    private Text affectedReplyName;
    @FXML
    private Text messageToAffected;
    @FXML
    private Text contactToAffected;
    @FXML
    private TextArea affectedReplyMessage;
    @FXML
    private TextArea affectedContactInfo;
    @FXML
    private Button approveOfferButton;
    @FXML
    private Button rejectOfferButton;

    public void setOfferId(int offerId) {
        this.offerId = offerId;
        affectedReplyName.setText(CommunicationService.getSourceName(offerId) + "'s offer");
        messageToAffected.setText(CommunicationService.getSourceDescription(offerId));
        contactToAffected.setText(CommunicationService.getSourceInfo(offerId));
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
        CommunicationService.closeRequest(VolunteerService.getOfferSourceUsername(offerId),loggedInAffected.getUsername(),  offerId, 'a', affectedReplyMessage.getText(), affectedContactInfo.getText());
        Volunteer destination = (Volunteer) UserService.getUser(((VolunteerItem)offersInboxTable.getSelectionModel().getSelectedItem()).getUsername());
        if (offersInboxTable != null)
            offersInboxTable.setItems(AffectedPageController.getOffersInbox(loggedInAffected.getUsername()));

        Stage stage = (Stage) approveOfferButton.getScene().getWindow();
        stage.close();
        destination.setNewHistory(true);
        UserService.updateUserInDatabase(destination);
        JOptionPane.showMessageDialog(null, VolunteerService.getOfferName(offerId) + " offer approved", "Successfully approved offer", 1);
    }

    public void handleRejectOffer(javafx.event.ActionEvent event){
        CommunicationService.closeRequest(VolunteerService.getOfferSourceUsername(offerId),loggedInAffected.getUsername(), offerId, 'r', affectedReplyMessage.getText(), affectedContactInfo.getText());
        Volunteer destination = (Volunteer) UserService.getUser(((VolunteerItem)offersInboxTable.getSelectionModel().getSelectedItem()).getUsername());
        if (offersInboxTable != null)
            offersInboxTable.setItems(AffectedPageController.getOffersInbox(loggedInAffected.getUsername()));
        Stage stage = (Stage) rejectOfferButton.getScene().getWindow();
        stage.close();
        destination.setNewHistory(true);
        UserService.updateUserInDatabase(destination);
        JOptionPane.showMessageDialog(null, VolunteerService.getOfferName(offerId) + " offer rejected", "Successfully rejected offer", 1);
    }

}