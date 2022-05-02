package org.ShelterMe.project.exceptions;

public class UsernameAlreadyExistsException extends Exception {

    private String username;

    public UsernameAlreadyExistsException(String username) {
        super(String.format("Error: An account with the following username already exists: %s", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
