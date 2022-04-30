package org.ShelterMe.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Affected extends User {

    public Affected(@JsonProperty("username") String username,@JsonProperty("password") String password,@JsonProperty("role") String role,@JsonProperty("fullName") String fullName,@JsonProperty("country") String country,@JsonProperty("phoneNumber") String phoneNumber) {
        super(username, password, role, fullName, country, phoneNumber);
    }

    public void openMainUserPage() throws IOException {

        Parent mainUserPage = FXMLLoader.load(getClass().getClassLoader().getResource("affectedPage.fxml"));
        Stage stage = new Stage();
        stage.setTitle("ShelterMe - "  + getUsername() + " (" + getRole() + ")");
        stage.getIcons().add(new Image("file:docs/Logo.png"));
        stage.setScene(new Scene(mainUserPage, 450, 450));
        stage.show();;

    }
}
