package org.ShelterMe.project.exceptions;

public class PhoneNumberFormatException extends Exception {

    private String phoneNumber;

    public PhoneNumberFormatException(String phoneNumber) {
        super("The entered phone number is not formatted correctly.");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
