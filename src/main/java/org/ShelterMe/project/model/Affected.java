package org.ShelterMe.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.ShelterMe.project.controllers.AffectedPageController;

import java.io.IOException;

import org.ShelterMe.project.services.AffectedService;

public class Affected extends User {

    private int requestsNo;
    private boolean newRequest;
    private boolean newHistory;
    public Affected(@JsonProperty("username") String username,@JsonProperty("password") String password,@JsonProperty("role") String role,@JsonProperty("fullName") String fullName,@JsonProperty("country") String country,@JsonProperty("phoneNumber") String phoneNumber) {
        super(username, password, role, fullName, country, phoneNumber);
        this.newHistory = false;
        this.newRequest = false;
    }

    public void openMainUserPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("affectedPage.fxml"));
        Parent mainUserPage = loader.load();
        AffectedPageController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("ShelterMe - "  + getFullName() + " (" + getRole() + ")");
        stage.getIcons().add(new Image("file:docs/Logo.png"));
        stage.setScene(new Scene(mainUserPage, 750, 500));
        stage.show();
        stage.setResizable(false);
        controller.setSignedInAs(this);
        mainUserPage.requestFocus();
    }

    public void calculateValues(){
        requestsNo = AffectedService.getAffectedRequestsNumber(username);
    }

    public int getRequestsNo() {
        return requestsNo;
    }


    public boolean isNewHistory() {
        return newHistory;
    }

    public void setNewHistory(boolean newHistory) {
        this.newHistory = newHistory;
    }

    public boolean isNewRequest() {
        return newRequest;
    }

    public void setNewRequest(boolean newRequest) {
        this.newRequest = newRequest;
    }
}
