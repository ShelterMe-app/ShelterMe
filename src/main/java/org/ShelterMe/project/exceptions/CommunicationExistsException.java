package org.ShelterMe.project.exceptions;

public class CommunicationExistsException extends Exception{
    public CommunicationExistsException(String type, String destinationType) {
        super("Error: A " + type + " can be sent only once to the same " +  destinationType);
    }
}
