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

    public Communication(char type, String sourceUsername, String destinationUsername, int id) {
        this.communicationId = CommunicationService.getCounter() + 1;
        this.type = type;
        this.sourceUsername = sourceUsername;
        this.destinationUsername = destinationUsername;
        this.id = id;
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
}
