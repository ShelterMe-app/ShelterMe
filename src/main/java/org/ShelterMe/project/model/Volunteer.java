package org.ShelterMe.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ShelterMe.project.controllers.VolunteerPageController;
import org.ShelterMe.project.controllers.RegistrationController;

import java.io.IOException;

public class Volunteer extends User {

    public Volunteer(@JsonProperty("username") String username,@JsonProperty("password") String password,@JsonProperty("role") String role,@JsonProperty("fullName") String fullName,@JsonProperty("country") String country,@JsonProperty("phoneNumber") String phoneNumber) {
        super(username, password, role, fullName, country, phoneNumber);
    }

    public void openMainUserPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("volunteerPage.fxml"));
        Parent mainUserPage = loader.load();
        VolunteerPageController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("ShelterMe - "  + getFullName() + " (" + getRole() + ")");
        stage.getIcons().add(new Image("file:docs/Logo.png"));
        stage.setScene(new Scene(mainUserPage, 750, 500));
        stage.show();
        stage.setResizable(false);
        controller.setSignedInAs(getFullName());
        mainUserPage.requestFocus();
    }
}
