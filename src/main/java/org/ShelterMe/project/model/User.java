package org.ShelterMe.project.model;

import org.dizitart.no2.objects.Id;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Affected.class, name = "Affected"),

        @JsonSubTypes.Type(value = Volunteer.class, name = "Volunteer") }
)
abstract public class User {
    @Id
    protected String username;
    private String password;
    private String role;
    private String fullName;
    private String country;
    private String phoneNumber;
    private int currentFailedAttempts;
    private Date lockedInUntil;
    private boolean isLocked;

    public User(String username, String password, String role, String fullName, String country, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCurrentFailedAttempts() {
        return currentFailedAttempts;
    }

    public void setCurrentFailedAttempts(int currentFailedAttempts) {
        this.currentFailedAttempts = currentFailedAttempts;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Date getLockedInUntil() {
        return lockedInUntil;
    }

    public void setLockedInUntil(Date lockedInUntil) {
        this.lockedInUntil = lockedInUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password) && role.equals(user.role) && fullName.equals(user.fullName) && country.equals(user.country) && phoneNumber.equals(user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role, fullName, country, phoneNumber);
    }

    abstract public void openMainUserPage() throws IOException;

    abstract public void calculateValues();

}
