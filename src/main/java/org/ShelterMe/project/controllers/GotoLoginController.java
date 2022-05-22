package org.ShelterMe.project.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

abstract class GotoLoginController {

    public static void goToLogin(Stage stage, javafx.event.ActionEvent event) throws IOException {
        if (stage != null)
            stage.close();

        Parent login = FXMLLoader.load(GotoLoginController.class.getClassLoader().getResource("login.fxml"));
        Scene scene = new Scene(login, 700, 500);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (appStage != null) {
            appStage.setScene(scene);
            appStage.setTitle("ShelterMe");
            appStage.show();
        }
    }
}
