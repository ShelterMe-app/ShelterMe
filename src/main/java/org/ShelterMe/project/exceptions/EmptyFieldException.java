package org.ShelterMe.project.exceptions;

public class EmptyFieldException extends Exception{
    public EmptyFieldException(String field) {
        super(String.format("Error: One of the fields is empty. Enter info for field: %s", field));
    }
}
