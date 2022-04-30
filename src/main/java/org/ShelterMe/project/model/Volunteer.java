package org.ShelterMe.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Volunteer extends User {
    public Volunteer(@JsonProperty("username") String username,@JsonProperty("password") String password,@JsonProperty("role") String role,@JsonProperty("fullName") String fullName,@JsonProperty("country") String country,@JsonProperty("phoneNumber") String phoneNumber) {
        super(username, password, role, fullName, country, phoneNumber);
    }

    public void openMainUserPage() throws IOException {

    }
}
