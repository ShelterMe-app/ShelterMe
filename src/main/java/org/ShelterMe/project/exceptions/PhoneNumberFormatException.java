package org.ShelterMe.project.exceptions;

public class PhoneNumberFormatException extends Exception {

    private String phoneNumber;

    public PhoneNumberFormatException(String phoneNumber, String country) {
        super(String.format("Error: The entered phone number is not formatted correctly for the country chosen: %s", country));
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
