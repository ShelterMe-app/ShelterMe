package org.ShelterMe.project.exceptions;

public class EmptyFieldException extends Exception{
    public EmptyFieldException() {
        super("Error: One of the fields is empty. Enter info for all fields.");
    }
}
