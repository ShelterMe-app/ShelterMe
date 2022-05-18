package org.ShelterMe.project.model;

import org.ShelterMe.project.services.CommunicationService;
import org.dizitart.no2.objects.Id;

public class Communication {
    @Id
    private int communicationId;
    private char type; // r - request, o - offer
    private String sourceUsername;
    private String destinationUsername;
    private int id; // id of request or offer
    private char status; // p - pending, a - accepted, r - rejected
    private String sourceMessage;
    private String destinationMessage;
    private String sourceContactMethods;
    private String destinationContactMethods;
    private Boolean isInHistory;


    public Communication(char type, String sourceUsername, String destinationUsername, int id, char status, String sourceMessage, String destinationMessage, String sourceContactMethods, String destinationContactMethods) {
        this.communicationId = CommunicationService.getCounter() + 1;
        this.type = type;
        this.sourceUsername = sourceUsername;
        this.destinationUsername = destinationUsername;
        this.id = id;
        this.status = status;
        this.sourceMessage = sourceMessage;
        this.destinationMessage = destinationMessage;
        this.sourceContactMethods = sourceContactMethods;
        this.destinationContactMethods = destinationContactMethods;
        this.isInHistory = false;
    }

    public Communication() {

    }

    public char isType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public void setDestinationUsername(String destinationUsername) {
        this.destinationUsername = destinationUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getSourceMessage() {
        return sourceMessage;
    }

    public void setSourceMessage(String sourceMessage) {
        this.sourceMessage = sourceMessage;
    }

    public String getDestinationMessage() {
        return destinationMessage;
    }

    public void setDestinationMessage(String destinationMessage) {
        this.destinationMessage = destinationMessage;
    }

    public String getSourceContactMethods() {
        return sourceContactMethods;
    }

    public void setSourceContactMethods(String sourceContactMethods) {
        this.sourceContactMethods = sourceContactMethods;
    }

    public String getDestinationContactMethods() {
        return destinationContactMethods;
    }

    public void setDestinationContactMethods(String destinationContactMethods) {
        this.destinationContactMethods = destinationContactMethods;
    }

    public Boolean getInHistory() {
        return isInHistory;
    }

    public void setInHistory(Boolean inHistory) {
        isInHistory = inHistory;
    }
}
