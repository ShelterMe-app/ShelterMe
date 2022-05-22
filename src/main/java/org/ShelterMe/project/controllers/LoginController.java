package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.EmptyFieldException;
import org.ShelterMe.project.exceptions.IncorrectPasswordException;
import org.ShelterMe.project.exceptions.LockedAccountException;
import org.ShelterMe.project.exceptions.UsernameDoesNotExistException;
import org.ShelterMe.project.services.UserService;
import org.ShelterMe.project.model.User;

import java.io.IOException;

import java.util.Date;

public class LoginController {

    @FXML
    private Text loginMessage;
    @FXML private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink registerPressed;
    @FXML
    private Button loginButton;

    public void handleLoginAction(javafx.event.ActionEvent event){
        User connectedUser = null;
        try{
            connectedUser = UserService.verifyLogin(usernameField.getText(), passwordField.getText());
            connectedUser.setCurrentFailedAttempts(0);
            UserService.updateUserInDatabase(connectedUser);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            connectedUser.calculateValues();
            connectedUser.openMainUserPage();
        } catch(IncorrectPasswordException e) {
            connectedUser = e.getUser();
            connectedUser.setCurrentFailedAttempts(connectedUser.getCurrentFailedAttempts() + 1);
            UserService.updateUserInDatabase(connectedUser);
            if (connectedUser.getCurrentFailedAttempts() == 5 && connectedUser.isLocked() == false) {
                connectedUser.setCurrentFailedAttempts(0);
                connectedUser.setLockedInUntil(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
                connectedUser.setLocked(true);
                UserService.updateUserInDatabase(connectedUser);
                loginMessage.setText("You have exhausted all attempts. You can try again after: " + connectedUser.getLockedInUntil().toString());
            } else
                loginMessage.setText(e.getMessage());
        } catch (LockedAccountException | EmptyFieldException | UsernameDoesNotExistException | IOException e) {
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
        appStage.setResizable(false);
    }
}
