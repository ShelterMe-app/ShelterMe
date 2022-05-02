package org.ShelterMe.project.exceptions;

public class WeakPasswordException extends Exception{
    public WeakPasswordException(String reason) {
        super(String.format("Password is weak: %s", reason));
    }
}
