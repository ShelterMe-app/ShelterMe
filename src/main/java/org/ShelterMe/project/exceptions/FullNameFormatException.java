package org.ShelterMe.project.exceptions;

public class FullNameFormatException extends Exception{
    public FullNameFormatException(String reason) {
        super(String.format("Error: The entered full name is not formatted correctly: %s", reason));
    }
}
