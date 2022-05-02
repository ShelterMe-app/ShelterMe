package org.ShelterMe.project.exceptions;
import org.ShelterMe.project.model.User;

public class LockedAccountException extends Exception{

    private User user;

    public LockedAccountException(User user) {
        super("You have exhausted all attempts. You can try again after: " + user.getLockedInUntil().toString());
        this.user = user;
    }
}
