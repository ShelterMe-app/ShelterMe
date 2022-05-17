package org.ShelterMe.project.exceptions;

public class QuantityFormatException extends Exception{
    public QuantityFormatException() {
        super(String.format("Quantity can not be negative or zero"));
    }
}