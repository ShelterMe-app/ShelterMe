package org.ShelterMe.project.exceptions;

import org.ShelterMe.project.model.User;

public class IncorrectPasswordException extends Exception{
    User user;

    public IncorrectPasswordException(User user)
    {
        super("Incorrect password!");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
