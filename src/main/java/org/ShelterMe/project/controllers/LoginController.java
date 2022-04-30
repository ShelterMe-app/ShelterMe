package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.EmptyFieldException;
import org.ShelterMe.project.exceptions.IncorrectPasswordException;
import org.ShelterMe.project.exceptions.UsernameDoesNotExistException;
import org.ShelterMe.project.services.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private Text loginMessage;
    @FXML private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink registerPressed;

    public void handleLoginAction(javafx.event.ActionEvent event){
        try{
            UserService.verifyLogin(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Correct credentials!");
        } catch (EmptyFieldException | UsernameDoesNotExistException | IncorrectPasswordException e) {
            loginMessage.setText(e.getMessage());
        }
    }

    public void handleRegisterMenu(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) registerPressed.getScene().getWindow();
        stage.close();

        Parent login = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
        Scene scene = new Scene(login);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }
}
