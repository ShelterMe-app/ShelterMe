package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.services.UserService;

public class RegistrationController {

    @FXML
    private Text registrationMessage;
    @FXML
    private TextField fullName;
    @FXML
    private TextField address;
    @FXML
    private TextField phoneNumber;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;

    @FXML
    public void initialize() {
        role.getItems().addAll("Affected", "Volunteer");
    }

    @FXML
    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue(), fullName.getText(), address.getText(), phoneNumber.getText());
            registrationMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException | EmptyFieldException | PhoneNumberFormatException | WeakPasswordException | FullNameFormatException e) {
            registrationMessage.setText(e.getMessage());
        }
    }
}
